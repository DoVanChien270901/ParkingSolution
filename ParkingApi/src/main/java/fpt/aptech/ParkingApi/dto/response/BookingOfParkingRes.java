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
public class BookingOfParkingRes {

    private int id;
    private LocalDateTime starttime;
    private Integer timenumber;
    private double price;
    private String carname;
    private String lisenceplate;
    private String locationcode;

    public BookingOfParkingRes() {
    }

    public BookingOfParkingRes(int id, LocalDateTime starttime, Integer timenumber, double price, String carname, String lisenceplate, String locationcode) {
        this.id = id;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.price = price;
        this.carname = carname;
        this.lisenceplate = lisenceplate;
        this.locationcode = locationcode;
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

    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public Integer getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(Integer timenumber) {
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

    public String getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }

    public String getLisenceplate() {
        return lisenceplate;
    }

    public void setLisenceplate(String lisenceplate) {
        this.lisenceplate = lisenceplate;
    }

}
