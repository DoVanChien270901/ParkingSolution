/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.request;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
public class AddRevenueReq {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private Double amount;
    private String Parkingname;

    public AddRevenueReq() {
    }

    public AddRevenueReq(LocalDate date, Double amount, String Parkingname) {
        this.date = date;
        this.amount = amount;
        this.Parkingname = Parkingname;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getParkingname() {
        return Parkingname;
    }

    public void setParkingname(String Parkingname) {
        this.Parkingname = Parkingname;
    }

}
