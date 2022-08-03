/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "parkinglocation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parkinglocation.findAll", query = "SELECT p FROM Parkinglocation p"),
    @NamedQuery(name = "Parkinglocation.findByName", query = "SELECT p FROM Parkinglocation p WHERE p.name = :name"),
    @NamedQuery(name = "Parkinglocation.findByLatitude", query = "SELECT p FROM Parkinglocation p WHERE p.latitude = :latitude"),
    @NamedQuery(name = "Parkinglocation.findByLongtitude", query = "SELECT p FROM Parkinglocation p WHERE p.longtitude = :longtitude"),
    @NamedQuery(name = "Parkinglocation.findByAddress", query = "SELECT p FROM Parkinglocation p WHERE p.address = :address"),
    @NamedQuery(name = "Parkinglocation.findByNop", query = "SELECT p FROM Parkinglocation p WHERE p.nop = :nop"),
    @NamedQuery(name = "Parkinglocation.findByRentcost", query = "SELECT p FROM Parkinglocation p WHERE p.rentcost = :rentcost")})
public class Parkinglocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "latitude")
    private String latitude;
    @Size(max = 255)
    @Column(name = "longtitude")
    private String longtitude;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Column(name = "columnofrow")
    private Integer columnofrow;
    @Column(name = "nop")
    private Integer nop;
    @Column(name = "blank")
    private Integer blank;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rentcost")
    private double rentcost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parkingname")
    private Collection<Booking> bookingCollection;
    @OneToMany(mappedBy = "parkingname")
    private Collection<Transactioninformation> transactioninformationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parkingname")
    private Collection<Parkinghistory> parkinghistoryCollection;

    public Parkinglocation() {
    }

    public Parkinglocation(String name) {
        this.name = name;
    }

    public Parkinglocation(String name, double rentcost) {
        this.name = name;
        this.rentcost = rentcost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getColumnofrow() {
        return columnofrow;
    }

    public void setColumnofrow(Integer columnofrow) {
        this.columnofrow = columnofrow;
    }

    public Integer getNop() {
        return nop;
    }

    public void setNop(Integer nop) {
        this.nop = nop;
    }

    public Integer getBlank() {
        return blank;
    }

    public void setBlank(Integer blank) {
        this.blank = blank;
    }

    public double getRentcost() {
        return rentcost;
    }

    public void setRentcost(double rentcost) {
        this.rentcost = rentcost;
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
    public Collection<Parkinghistory> getParkinghistoryCollection() {
        return parkinghistoryCollection;
    }

    public void setParkinghistoryCollection(Collection<Parkinghistory> parkinghistoryCollection) {
        this.parkinghistoryCollection = parkinghistoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parkinglocation)) {
            return false;
        }
        Parkinglocation other = (Parkinglocation) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Parkinglocation[ name=" + name + " ]";
    }

}
