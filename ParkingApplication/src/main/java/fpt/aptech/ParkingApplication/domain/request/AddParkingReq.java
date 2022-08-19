/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author CHIEN
 */
public class AddParkingReq {

    @Size(min = 6, max = 150, message = "*Name must between 6 and 20 characters !")
    private String name;
    @Size(min = 6, max = 50, message = "*Latitude can not be blank !")
    @NotNull(message = "*Latitude can not be blank !")
    private String latitude;
    @NotNull(message = "*Longitude can not be blank !")
    @Size(min = 6, max = 50, message = "*Longitude can not be blank !")
    private String longtitude;
    @Size(min = 6, max = 250, message = "*Address can not be blank !")
    private String address;
    @NotNull(message = "*Parking Spaces can not be blank !")
    private Integer nop;
    @NotNull(message = "*Rent Cost can not be blank !")
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
