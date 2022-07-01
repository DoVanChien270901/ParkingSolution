package fpt.aptech.parkinggo.domain.response;

import fpt.aptech.parkinggo.domain.enumm.Roles;

public class LoginRes {
    private String token;
    private String fullname;
    private String email;
    private Roles role;

    public LoginRes() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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


}