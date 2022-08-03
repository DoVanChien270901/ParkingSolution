/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.request;

import java.time.LocalDateTime;

/**
 *
 * @author CHIEN
 */
public class BookingReq {

    private LocalDateTime starttime;
    private int timenumber;
    private String locationcode;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BookingReq() {
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(String  starttime) {
        this.starttime = LocalDateTime.parse(starttime);
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

}
