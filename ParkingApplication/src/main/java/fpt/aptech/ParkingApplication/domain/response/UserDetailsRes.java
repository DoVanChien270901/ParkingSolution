/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.response;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author CHIEN
 */
public class UserDetailsRes {

    private Integer identitycard;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private boolean gender;
    private LocalDate dob;
    private Double balance;
    private String email;
    private Integer phone;

    public UserDetailsRes() {
    }

    public UserDetailsRes(Integer identitycard, String fullname, boolean gender, LocalDate dob, Double balance, String email, Integer phone) {
        this.identitycard = identitycard;
        this.fullname = fullname;
        this.gender = gender;
        this.dob = dob;
        this.balance = balance;
        this.email = email;
        this.phone = phone;
    }

    public Integer getIdentitycard() {
        return identitycard;
    }

    public void setIdentitycard(Integer identitycard) {
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

}
