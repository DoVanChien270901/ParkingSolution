/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.qrcontent;

/**
 *
 * @author CHIEN
 */
public class ProfileQrContent {
    private String fullname;
    private Double balance;
    private int identitycard;

    public ProfileQrContent() {
    }
    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(int identitycard) {
        this.identitycard = identitycard;
    }
    
    
}
