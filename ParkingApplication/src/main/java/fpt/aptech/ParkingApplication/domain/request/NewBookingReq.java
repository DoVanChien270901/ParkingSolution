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
public class NewBookingReq {

    private String username;
    private String starttime;
    private int timenumber;
    private String locationcode;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private boolean walletparking;

    public NewBookingReq() {
    }

    public NewBookingReq(String username, String starttime, int timenumber, String locationcode, String carname, String lisenceplates, String parkingname, boolean walletparking) {
        this.username = username;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.locationcode = locationcode;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
        this.parkingname = parkingname;
        this.walletparking = walletparking;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
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

    public boolean isWalletparking() {
        return walletparking;
    }

    public void setWalletparking(boolean walletparking) {
        this.walletparking = walletparking;
    }

}
