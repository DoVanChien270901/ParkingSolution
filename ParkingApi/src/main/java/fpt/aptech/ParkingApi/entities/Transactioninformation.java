/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "transactioninformation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactioninformation.findAll", query = "SELECT t FROM Transactioninformation t"),
    @NamedQuery(name = "Transactioninformation.findByTransno", query = "SELECT t FROM Transactioninformation t WHERE t.transno = :transno"),
    @NamedQuery(name = "Transactioninformation.findByAmount", query = "SELECT t FROM Transactioninformation t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transactioninformation.findByChannel", query = "SELECT t FROM Transactioninformation t WHERE t.channel = :channel"),
    @NamedQuery(name = "Transactioninformation.findByDatetime", query = "SELECT t FROM Transactioninformation t WHERE t.datetime = :datetime"),
    @NamedQuery(name = "Transactioninformation.findByDescription", query = "SELECT t FROM Transactioninformation t WHERE t.description = :description"),
    @NamedQuery(name = "Transactioninformation.findByStatuscode", query = "SELECT t FROM Transactioninformation t WHERE t.statuscode = :statuscode"),
    @NamedQuery(name = "Transactioninformation.findByStype", query = "SELECT t FROM Transactioninformation t WHERE t.stype = :stype")})
public class Transactioninformation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "transno")
    private String transno;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Size(max = 25)
    @Column(name = "channel")
    private String channel;
    @Column(name = "datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datetime;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "statuscode")
    private Integer statuscode;
    @Size(max = 50)
    @Column(name = "stype")
    private String stype;
    @JoinColumn(name = "parkingname", referencedColumnName = "name")
    @ManyToOne
    private Parkinglocation parkingname;
    @JoinColumn(name = "accountid", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Profile accountid;

    public Transactioninformation() {
    }

    public Transactioninformation(String transno) {
        this.transno = transno;
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public Parkinglocation getParkingname() {
        return parkingname;
    }

    public void setParkingname(Parkinglocation parkingname) {
        this.parkingname = parkingname;
    }

    public Profile getAccountid() {
        return accountid;
    }

    public void setAccountid(Profile accountid) {
        this.accountid = accountid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transno != null ? transno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactioninformation)) {
            return false;
        }
        Transactioninformation other = (Transactioninformation) object;
        if ((this.transno == null && other.transno != null) || (this.transno != null && !this.transno.equals(other.transno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Transactioninformation[ transno=" + transno + " ]";
    }

}
