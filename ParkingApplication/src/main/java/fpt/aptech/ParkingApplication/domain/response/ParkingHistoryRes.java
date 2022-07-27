/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
public class ParkingHistoryRes {
    private Integer id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime starttime;
    private Integer timenumber;
    private String carname;
    private String lisenceplates;
    private String parkingname;

    public ParkingHistoryRes() {
    }

    public ParkingHistoryRes(Integer id, LocalDateTime starttime, Integer timenumber, String carname, String lisenceplates, String parkingname) {
        this.id = id;
        this.starttime = starttime; 
        this.timenumber = timenumber;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
        this.parkingname = parkingname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
