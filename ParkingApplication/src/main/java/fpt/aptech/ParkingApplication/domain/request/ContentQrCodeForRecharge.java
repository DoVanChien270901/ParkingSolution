/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.request;

import java.io.Serializable;

/**
 *
 * @author CHIEN
 */
public class ContentQrCodeForRecharge implements Serializable {

    private String username;
    private double amount;

    public ContentQrCodeForRecharge() {
    }

    public ContentQrCodeForRecharge(String username, double amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
