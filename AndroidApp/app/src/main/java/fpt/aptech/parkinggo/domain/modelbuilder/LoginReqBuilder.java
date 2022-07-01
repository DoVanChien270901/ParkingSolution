package fpt.aptech.parkinggo.domain.modelbuilder;

import fpt.aptech.parkinggo.domain.request.LoginReq;

public class LoginReqBuilder {
    private String username;
    private String password;

    public LoginReqBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginReqBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginReq createLoginReq() {
        return new LoginReq(username, password);
    }
}