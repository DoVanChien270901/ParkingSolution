/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p"),
    @NamedQuery(name = "Profile.findByUsername", query = "SELECT p FROM Profile p WHERE p.username = :username"),
    @NamedQuery(name = "Profile.findByBalance", query = "SELECT p FROM Profile p WHERE p.balance = :balance"),
    @NamedQuery(name = "Profile.findByDob", query = "SELECT p FROM Profile p WHERE p.dob = :dob"),
    @NamedQuery(name = "Profile.findByEmail", query = "SELECT p FROM Profile p WHERE p.email = :email"),
    @NamedQuery(name = "Profile.findByFullname", query = "SELECT p FROM Profile p WHERE p.fullname = :fullname"),
    @NamedQuery(name = "Profile.findByGender", query = "SELECT p FROM Profile p WHERE p.gender = :gender"),
    @NamedQuery(name = "Profile.findByIdentitycard", query = "SELECT p FROM Profile p WHERE p.identitycard = :identitycard"),
    @NamedQuery(name = "Profile.findByPhone", query = "SELECT p FROM Profile p WHERE p.phone = :phone")})
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "username")
    private String username;
    @Column(name = "dob")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "phone")
    private Integer phone;
    @Basic(optional = false)
    @NotNull
    @Column(name = "balance")
    private double balance;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "fullname")
    private String fullname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "identitycard")
    private int identitycard;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountid")
    private Collection<Booking> bookingCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountid")
    private Collection<Transactioninformation> transactioninformationCollection;
    @OneToMany(mappedBy = "accountid")
    private Collection<Qrcode> qrcodeCollection;

    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @OneToOne(optional = true)
    private Account account;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountid")
    private Collection<Parkinghistory> parkinghistoryCollection;

    public Profile() {
    }

    public Profile(String username) {
        this.username = username;
    }

    public Profile(String username, double balance, int identitycard) {
        this.username = username;
        this.balance = balance;
        this.identitycard = identitycard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Booking> getBookingCollection() {
        return bookingCollection;
    }

    public void setBookingCollection(Collection<Booking> bookingCollection) {
        this.bookingCollection = bookingCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Transactioninformation> getTransactioninformationCollection() {
        return transactioninformationCollection;
    }

    public void setTransactioninformationCollection(Collection<Transactioninformation> transactioninformationCollection) {
        this.transactioninformationCollection = transactioninformationCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Qrcode> getQrcodeCollection() {
        return qrcodeCollection;
    }

    public void setQrcodeCollection(Collection<Qrcode> qrcodeCollection) {
        this.qrcodeCollection = qrcodeCollection;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Parkinghistory> getParkinghistoryCollection() {
        return parkinghistoryCollection;
    }

    public void setParkinghistoryCollection(Collection<Parkinghistory> parkinghistoryCollection) {
        this.parkinghistoryCollection = parkinghistoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Profile[ username=" + username + " ]";
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(int identitycard) {
        this.identitycard = identitycard;
    }

}
