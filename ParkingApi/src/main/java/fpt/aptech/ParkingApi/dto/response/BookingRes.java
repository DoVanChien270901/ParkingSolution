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
public class BookingRes {

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

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

}
