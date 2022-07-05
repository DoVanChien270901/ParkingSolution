/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.request;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import java.io.Serializable;

/**
 *
 * @author vantu
 */
public class RechargeByQrCodeReq implements Serializable {

    private Double amount;
    private String username = null;

    public RechargeByQrCodeReq() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
