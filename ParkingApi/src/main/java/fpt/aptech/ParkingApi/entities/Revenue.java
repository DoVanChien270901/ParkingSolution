/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
@Table(name = "revenue")
@XmlRootElement
//get Name in month
@NamedNativeQuery(
        name = "getParkingNameInMonth",
        query = "select parkingname as parkingname from revenue where DATEPART(Month, date) = :month and DATEPART(Year, date) = :year group by parkingname",
        resultSetMapping = "ParkingName"
)
//get Name in year
@NamedNativeQuery(
        name = "getParkingNameInYear",
        query = "select parkingname as parkingname from revenue where DATEPART(Year, date) = :year group by parkingname",
        resultSetMapping = "ParkingName"
)
@SqlResultSetMapping(
        name = "ParkingName",
        classes = @ConstructorResult(targetClass = ParkingName.class,
                columns = {
                    @ColumnResult(name = "parkingname", type = String.class)
                })
)
//getRevenue by day in month/year
@NamedNativeQuery(
        name = "getRevenueDayInMonth",
        query = "select parkingname, DATEPART(Day, date) day, SUM(amount) AS sumD\n"
        + "from revenue where DATEPART(Month, date) = :month and DATEPART(Year, date) = :year group by parkingname, DATEPART(Day, date);",
        resultSetMapping = "CustomeResultRevenueDayInMonth"
)
@SqlResultSetMapping(
        name = "CustomeResultRevenueDayInMonth",
        classes = @ConstructorResult(targetClass = RevenueByDay.class,
                columns = {
                    @ColumnResult(name = "parkingname", type = String.class),
                    @ColumnResult(name = "day", type = Integer.class),
                    @ColumnResult(name = "sumD", type = Double.class)
                })
)
// getRevenue by month in year
@NamedNativeQuery(
        name = "getRevenueMonthInYear",
        query = "select parkingname, DATEPART(Month, date) month, SUM(amount) AS sumM\n"
        + "from revenue where DATEPART(Year, date) = :year group by parkingname, DATEPART(Month, date);",
        resultSetMapping = "CustomeResultRevenue"
)
@SqlResultSetMapping(
        name = "CustomeResultRevenue",
        classes = @ConstructorResult(targetClass = RevenueByMoth.class,
                columns = {
                    @ColumnResult(name = "parkingname", type = String.class),
                    @ColumnResult(name = "month", type = Integer.class),
                    @ColumnResult(name = "sumM", type = Double.class)
                })
)

//default
@NamedQueries({
    @NamedQuery(name = "Revenue.findAll", query = "SELECT r FROM Revenue r"),
    @NamedQuery(name = "Revenue.findById", query = "SELECT r FROM Revenue r WHERE r.id = :id"),
    @NamedQuery(name = "Revenue.findByDate", query = "SELECT r FROM Revenue r WHERE r.date = :date"),
    @NamedQuery(name = "Revenue.findByAmount", query = "SELECT r FROM Revenue r WHERE r.amount = :amount"),
    @NamedQuery(name = "Revenue.findByParkingname", query = "SELECT r FROM Revenue r WHERE r.parkingname = :parkingname")})
public class Revenue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Size(max = 100)
    @Column(name = "parkingname")
    private String parkingname;

    public Revenue() {
    }

    public Revenue(Integer id) {
        this.id = id;
    }

    public Revenue(Integer id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
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
        if (!(object instanceof Revenue)) {
            return false;
        }
        Revenue other = (Revenue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fpt.aptech.ParkingApi.entities.Revenue[ id=" + id + " ]";
    }

}
