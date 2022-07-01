/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.domain.request;

import javax.validation.constraints.*;
import javax.validation.constraints.Size;

/**
 *
 * @author CHIEN
 */
public class AuthenticateReq {

    @Size(min = 6, max = 25, message = "Username must be between 6 and 25 characters")
    private String username;
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    private String password;

    public AuthenticateReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
