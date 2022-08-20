/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.dto.response;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
public class UserDetailsRes {

    private String identitycard;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private boolean gender;
    private LocalDate dob;
    private Double balance;
    private String email;
    private String phone;

    public UserDetailsRes() {
    }

    public UserDetailsRes(String identitycard, String fullname, boolean gender, LocalDate dob, Double balance, String email, String phone) {
        this.identitycard = identitycard;
        this.fullname = fullname;
        this.gender = gender;
        this.dob = dob;
        this.balance = balance;
        this.email = email;
        this.phone = phone;
    }

    public String getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(String identitycard) {
        this.identitycard = identitycard;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
