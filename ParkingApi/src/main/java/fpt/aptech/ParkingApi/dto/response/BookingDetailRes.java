/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author CHIEN
 */
public class BookingDetailRes {

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

    public BookingDetailRes(LocalDateTime starttime, int timenumber, double price, String carname, String lisenceplates, String parkingname, String locationcode) {
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.price = price;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
        this.parkingname = parkingname;
        this.locationcode = locationcode;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
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
