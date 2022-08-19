/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author CHIEN
 */
public class BookingReq {
//    @Size(min = 6, max = 25, message = "*Car name must be between 6 and 25 characters!")
    private String starttime;
    @Min(value = 1, message = "*Timenumber is required!")
    @Max(value = 24, message = "*Timenumber must be less than twenty-four!")
    private int timenumber;
    @NotBlank(message = "*Chose location code in parking!")
    private String locationcode;
    @Size(min = 6, max = 25, message = "*Car name must be between 6 and 25 characters!")
    private String carname;
    @Size(min = 6, max = 10, message = "*Lisence plates is invalid!")
    private String lisenceplates;
//    @Size(min = 6, max = 25, message = "*Car name must be between 6 and 25 characters!")
    private String parkingname;
    @NotBlank(message = "*Select payment method!")
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BookingReq() {
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        LocalDateTime lcdt = LocalDateTime.parse(starttime, formatter);
        starttime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(lcdt);
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

}
