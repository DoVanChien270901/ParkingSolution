package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingRes implements Serializable {
    private int id;
    private LocalDateTime starttime;
    private int timenumber;
    private double price;
    private String parkingname;

    public BookingRes() {
    }

    public BookingRes(int id, LocalDateTime starttime, int timenumber, double price, String parkingname) {
        this.id = id;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.price = price;
        this.parkingname = parkingname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = LocalDateTime.parse(starttime);
    }

    public int getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(int timenumber) {
        this.timenumber = timenumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

}
