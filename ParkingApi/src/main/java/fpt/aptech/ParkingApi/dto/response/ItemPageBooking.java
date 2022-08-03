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
public class ItemPageBooking {
    private int id;
    private LocalDateTime starttime;
    private int timenumber;
    private String locationcode;
    private double price;
    private String carname;
    private String lisenceplates;

    public ItemPageBooking() {
    }

    public ItemPageBooking(int id, LocalDateTime starttime, int timenumber, String locationcode, double price, String carname, String lisenceplates) {
        this.id = id;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.locationcode = locationcode;
        this.price = price;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
