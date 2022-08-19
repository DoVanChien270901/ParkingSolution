/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.request;

import java.io.Serializable;

/**
 *
 * @author CHIEN
 */
public class AddParkingReq implements Serializable{

    private String name;
    private String latitude;
    private String longtitude;
    private String address;
    private Integer nop;
    private Integer rentcost;
    private Integer blank;

    public AddParkingReq() {
    }

    public AddParkingReq(String name, String latitude, String longtitude, String address, Integer nop, Integer blank, Integer rentcost) {
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

    public Integer getNop() {
        return nop;
    }

    public void setNop(Integer nop) {
        this.nop = nop;
    }

    public Integer getBlank() {
        return blank;
    }

    public void setBlank(Integer blank) {
        this.blank = blank;
    }

    public Integer getRentcost() {
        return rentcost;
    }

    public void setRentcost(Integer rentcost) {
        this.rentcost = rentcost;
    }

}
