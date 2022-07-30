package fpt.aptech.parkinggo.domain.modelbuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.domain.request.NewBookingReq;

public class NewBookingReqBuilder {
    private String username;
    private String starttime;
    private int timenumber;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private boolean walletparking;

    public NewBookingReqBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public NewBookingReqBuilder setStarttime(String starttime) {
        this.starttime = LocalDateTime.parse(starttime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
        return this;
    }

    public NewBookingReqBuilder setTimenumber(int timenumber) {
        this.timenumber = timenumber;
        return this;
    }

    public NewBookingReqBuilder setCarname(String carname) {
        this.carname = carname;
        return this;
    }

    public NewBookingReqBuilder setLisenceplates(String lisenceplates) {
        this.lisenceplates = lisenceplates;
        return this;
    }

    public NewBookingReqBuilder setParkingname(String parkingname) {
        this.parkingname = parkingname;
        return this;
    }

    public NewBookingReqBuilder setWalletparking(boolean walletparking) {
        this.walletparking = walletparking;
        return this;
    }

    public NewBookingReq createBookEReq() {
        return new NewBookingReq(username, starttime, timenumber, carname, lisenceplates, parkingname, walletparking);
    }
}
