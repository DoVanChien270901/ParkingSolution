package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;

public class ParkingRes implements Serializable {
    private String name;
    private String latitude;
    private String longtitude;
    private String address;
    private int nop;
    private int blank;
    private double rentcost;

    public ParkingRes() {
    }

    public ParkingRes(String name, String latitude, String longtitude, String address, int nop, int blank, double rentcost) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.address = address;
        this.nop = nop;
        this.blank = blank;
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

    public int getNop() {
        return nop;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public int getBlank() {
        return blank;
    }

    public void setBlank(int blank) {
        this.blank = blank;
    }

    public double getRentcost() {
        return rentcost;
    }

    public void setRentcost(double rentcost) {
        this.rentcost = rentcost;
    }
}
