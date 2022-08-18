package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BookingDetailRes implements Serializable {
    private LocalDateTime starttime;
    private int timenumber;
    private double price;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private byte[] qrcontent;
    private String locationcode;

    public BookingDetailRes() {
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

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public byte[] getQrcontent() {
        return qrcontent;
    }

    public void setQrcontent(byte[] qrcontent) {
        this.qrcontent = qrcontent;
    }

    public String getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }
}
