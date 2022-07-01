/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "booking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findByStarttime", query = "SELECT b FROM Booking b WHERE b.starttime = :starttime"),
    @NamedQuery(name = "Booking.findByTimenumber", query = "SELECT b FROM Booking b WHERE b.timenumber = :timenumber"),
    @NamedQuery(name = "Booking.findByCarname", query = "SELECT b FROM Booking b WHERE b.carname = :carname"),
    @NamedQuery(name = "Booking.findByLisenceplates", query = "SELECT b FROM Booking b WHERE b.lisenceplates = :lisenceplates"),
    @NamedQuery(name = "Booking.findByStatus", query = "SELECT b FROM Booking b WHERE b.status = :status"),
    @NamedQuery(name = "Booking.findByCheckin", query = "SELECT b FROM Booking b WHERE b.checkin = :checkin"),
    @NamedQuery(name = "Booking.findByCheckout", query = "SELECT b FROM Booking b WHERE b.checkout = :checkout")})
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "starttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "timenumber")
    private Integer timenumber;
    @Size(max = 255)
    @Column(name = "carname")
    private String carname;
    @Size(max = 255)
    @Column(name = "lisenceplates")
    private String lisenceplates;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Column(name = "checkin")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkin;
    @Column(name = "checkout")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkout;
    @JoinColumn(name = "parkingname", referencedColumnName = "name")
    @ManyToOne(optional = false)
    private Parkinglocation parkingname;
    @JoinColumn(name = "accountid", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Profile accountid;

    public Booking() {
    }

    public Booking(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Integer getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(Integer timenumber) {
        this.timenumber = timenumber;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getLisenceplates() {
        return lisenceplates;
    }

    public void setLisenceplates(String lisenceplates) {
        this.lisenceplates = lisenceplates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDateTime checkin) {
        this.checkin = checkin;
    }

    public LocalDateTime getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDateTime checkout) {
        this.checkout = checkout;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Booking[ id=" + id + " ]";
    }

}
