package fpt.aptech.parkinggo.domain.response;

import fpt.aptech.parkinggo.domain.enumm.Roles;

public class LoginRes {
    private String token;
    private String username;
    private String fullname;
    private Double balance;
    private String email;
    private Roles role;
    private byte[] qrcode;

    public LoginRes() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public byte[] getQrcode() {
        return qrcode;
    }

    public void setQrcode(byte[] qrcode) {
        this.qrcode = qrcode;
    }
}