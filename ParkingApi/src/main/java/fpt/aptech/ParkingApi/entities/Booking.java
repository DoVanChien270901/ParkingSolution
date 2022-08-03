/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.format.annotation.DateTimeFormat;
import fpt.aptech.ParkingApi.dto.response.*;

/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "booking")
@XmlRootElement
//list-booking-by-parking
@NamedNativeQuery(
        name = "getListBookingByParkingName",
        query = "SELECT b.id AS id, b.starttime AS starttime, b.timenumber AS timenumber, "
        + "b.locationcode AS locationcode, b.price AS price, b.carname AS carname, "
        + "b.lisenceplates AS lisenceplates"
        + " FROM Booking b WHERE b.parkingname = :parkingname",
        resultSetMapping = "CustomeResultAllBooking"
)
//@SqlResultSetMapping(
//        name = "CustomeResultListBookingByParkingName",
//        classes = @ConstructorResult(targetClass = BookingOfParkingRes.class,
//                columns = {
//                    @ColumnResult(name = "id", type = Integer.class),
//                    @ColumnResult(name = "starttime", type = LocalDateTime.class),
//                    @ColumnResult(name = "timenumber", type = Integer.class),
//                    @ColumnResult(name = "price", type = Double.class),
//                    @ColumnResult(name = "carname", type = String.class),
//                    @ColumnResult(name = "lisenceplates", type = String.class),
//                    @ColumnResult(name = "locationcode", type = String.class)
//                })
//)
//booking-all-list
@NamedNativeQuery(
        name = "getListAllBooking",
        query = "SELECT b.id AS id, b.starttime AS starttime, b.timenumber AS timenumber, "
        + "b.locationcode AS locationcode, b.price AS price, b.carname AS carname, "
        + "b.lisenceplates AS lisenceplates"
        + " FROM Booking b",
        resultSetMapping = "CustomeResultAllBooking"
)
@SqlResultSetMapping(
        name = "CustomeResultAllBooking",
        classes = @ConstructorResult(targetClass = ItemPageBooking.class,
                columns = {
                    @ColumnResult(name = "id", type = Integer.class),
                    @ColumnResult(name = "starttime", type = LocalDateTime.class),
                    @ColumnResult(name = "timenumber", type = Integer.class),
                    @ColumnResult(name = "locationcode", type = String.class),
                    @ColumnResult(name = "price", type = Double.class),
                    @ColumnResult(name = "carname", type = String.class),
                    @ColumnResult(name = "lisenceplates", type = String.class)
                })
)
//booking-list-of-user?
@NamedNativeQuery(
        name = "getListBookingByUsername",
        query = "SELECT b.id AS id, b.starttime AS starttime, b.timenumber AS timenumber, b.price AS price, b.parkingname AS parkingname "
        + " FROM Booking b WHERE b.accountid = :username",
        resultSetMapping = "CustomeResultBookingList"
)
@SqlResultSetMapping(
        name = "CustomeResultBookingList",
        classes = @ConstructorResult(targetClass = BookingRes.class,
                columns = {
                    @ColumnResult(name = "id", type = Integer.class),
                    @ColumnResult(name = "starttime", type = LocalDateTime.class),
                    @ColumnResult(name = "timenumber", type = Integer.class),
                    @ColumnResult(name = "price", type = Double.class),
                    @ColumnResult(name = "parkingname", type = String.class)
                })
)
//booking-details
@NamedNativeQuery(
        name = "getDetailBookingById",
        query = "SELECT b.starttime AS starttime, b.timenumber AS timenumber, b.price AS price, b.carname AS carname, b.lisenceplates AS lisenceplates, b.parkingname AS parkingname "
        + " FROM Booking b WHERE b.id = :id",
        resultSetMapping = "CustomeResultBookingDetails"
)
@SqlResultSetMapping(
        name = "CustomeResultBookingDetails",
        classes = @ConstructorResult(targetClass = BookingDetailRes.class,
                columns = {
                    @ColumnResult(name = "starttime", type = LocalDateTime.class),
                    @ColumnResult(name = "timenumber", type = Integer.class),
                    @ColumnResult(name = "price", type = Double.class),
                    @ColumnResult(name = "carname", type = String.class),
                    @ColumnResult(name = "lisenceplates", type = String.class),
                    @ColumnResult(name = "parkingname", type = String.class)
                })
)
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime starttime;
    @Column(name = "timenumber")
    private Integer timenumber;
    @Size(max = 5)
    @Column(name = "locationcode")
    private String locationcode;
    @Column(name = "price")
    private double price;
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

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public Integer getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(Integer timenumber) {
        this.timenumber = timenumber;
    }

    public String getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
