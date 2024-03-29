/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
public class NewBookingReq {

    private String username;
    private LocalDateTime starttime;
    private int timenumber;
    private String locationcode;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private boolean walletparking;

    public NewBookingReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        try {
            this.starttime = LocalDateTime.parse(starttime);
        } catch (Exception e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.starttime = LocalDateTime.parse(starttime, formatter);
        }
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
