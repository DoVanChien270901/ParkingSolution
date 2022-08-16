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
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
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
import fpt.aptech.ParkingApi.dto.response.ParkingHistoryRes;
/**
 *
 * @author CHIEN
 */
@Entity
@Table(name = "parkinghistory")
@XmlRootElement
@NamedNativeQuery(
        name = "getAllParkingHistorySearch",
        query = "SELECT p.id AS id, p.starttime AS starttime, p.timenumber AS timenumber, p.carname AS carname, p.lisenceplates AS lisenceplates, "
            + "p.parkingname AS parkingname"
        + " FROM parkinghistory p WHERE p.parkingname = :parkingname and p.starttime >= :fromDate and p.starttime <= :toDate ORDER BY p.starttime DESC",
        resultSetMapping = "CustomeResultParkingHistoryList"
)
@NamedNativeQuery(
        name = "getAllParkingHistorySearchbyDate",
        query = "SELECT p.id AS id, p.starttime AS starttime, p.timenumber AS timenumber, p.carname AS carname, p.lisenceplates AS lisenceplates, "
            + "p.parkingname AS parkingname"
        + " FROM parkinghistory p WHERE p.starttime >= :fromDate and p.starttime <= :toDate ORDER BY p.starttime DESC",
        resultSetMapping = "CustomeResultParkingHistoryList"
)
@NamedNativeQuery(
        name = "getListParkingHistoryByParkingName",
        query = "SELECT p.id AS id, p.starttime AS starttime, p.timenumber AS timenumber, p.carname AS carname, p.lisenceplates AS lisenceplates, "
            + "p.parkingname AS parkingname"
        + " FROM parkinghistory p WHERE p.parkingname = :parkingname ORDER BY p.starttime DESC",
        resultSetMapping = "CustomeResultParkingHistoryList"
)
@NamedNativeQuery(
        name = "getListParkingHistoryByUsername",
        query = "SELECT p.id AS id, p.starttime AS starttime, p.timenumber AS timenumber, p.carname AS carname, p.lisenceplates AS lisenceplates, "
            + "p.parkingname AS parkingname"
        + " FROM parkinghistory p WHERE p.accountid = :username ORDER BY p.starttime DESC",
        resultSetMapping = "CustomeResultParkingHistoryList"
)
@NamedNativeQuery(
        name = "getListParkingHistoryByUsernameSearch",
        query = "SELECT p.id AS id, p.starttime AS starttime, p.timenumber AS timenumber, p.carname AS carname, p.lisenceplates AS lisenceplates, "
            + "p.parkingname AS parkingname"
        + " FROM parkinghistory p WHERE p.accountid = :username and p.starttime >= :fromDate and p.starttime <= :toDate ORDER BY p.starttime DESC",
        resultSetMapping = "CustomeResultParkingHistoryList"
)
@SqlResultSetMapping(
        name = "CustomeResultParkingHistoryList",
        classes = @ConstructorResult(targetClass = ParkingHistoryRes.class,
                columns = {
                    @ColumnResult(name = "id", type = Integer.class),
                    @ColumnResult(name = "starttime", type = LocalDateTime.class),
                    @ColumnResult(name = "timenumber", type = Integer.class),
                    @ColumnResult(name = "carname", type = String.class),
                    @ColumnResult(name = "lisenceplates", type = String.class),
                    @ColumnResult(name = "parkingname", type = String.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "Parkinghistory.findAll", query = "SELECT p FROM Parkinghistory p"),
    @NamedQuery(name = "Parkinghistory.findById", query = "SELECT p FROM Parkinghistory p WHERE p.id = :id"),
    @NamedQuery(name = "Parkinghistory.findByStarttime", query = "SELECT p FROM Parkinghistory p WHERE p.starttime = :starttime"),
    @NamedQuery(name = "Parkinghistory.findByTimenumber", query = "SELECT p FROM Parkinghistory p WHERE p.timenumber = :timenumber"),
    @NamedQuery(name = "Parkinghistory.findByCarname", query = "SELECT p FROM Parkinghistory p WHERE p.carname = :carname"),
    @NamedQuery(name = "Parkinghistory.findByLisenceplates", query = "SELECT p FROM Parkinghistory p WHERE p.lisenceplates = :lisenceplates")})
public class Parkinghistory implements Serializable {

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
    @Size(max = 100)
    @Column(name = "carname")
    private String carname;
    @Size(max = 50)
    @Column(name = "lisenceplates")
    private String lisenceplates;
    @JoinColumn(name = "parkingname", referencedColumnName = "name")
    @ManyToOne(optional = false)
    private Parkinglocation parkingname;
    @JoinColumn(name = "accountid", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Profile accountid;

    public Parkinghistory() {
    }

    public Parkinghistory(Integer id) {
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
        if (!(object instanceof Parkinghistory)) {
            return false;
        }
        Parkinghistory other = (Parkinghistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Parkinghistory[ id=" + id + " ]";
    }

}
