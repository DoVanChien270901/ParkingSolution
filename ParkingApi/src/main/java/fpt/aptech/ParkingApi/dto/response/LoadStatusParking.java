/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

import java.util.List;

/**
 *
 * @author CHIEN
 */
public class LoadStatusParking {

    private String parkingname;
    private int columnofrow;
    private String[] locationcode;
    private String[] codebooked;

    public LoadStatusParking() {
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public int getColumnofrow() {
        return columnofrow;
    }

    public void setColumnofrow(int columnofrow) {
        this.columnofrow = columnofrow;
    }

    public String[] getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String[] locationcode) {
        this.locationcode = locationcode;
    }

    public String[] getCodebooked() {
        return codebooked;
    }

    public void setCodebooked(String[] codebooked) {
        this.codebooked = codebooked;
    }

}
