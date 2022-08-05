package fpt.aptech.parkinggo.domain.request;

import java.time.LocalDateTime;

public class NewBookingReq {
    private String username;
    private String starttime;
    private String locationcode;
    private int timenumber;
    private String carname;
    private String lisenceplates;
    private String parkingname;
    private boolean walletparking;

    public NewBookingReq(String username, String starttime, int timenumber, String carname, String lisenceplates, String parkingname, boolean walletparking) {
        this.username = username;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
        this.parkingname = parkingname;
        this.walletparking = walletparking;
    }

    public NewBookingReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String locationcode) {
        this.locationcode = locationcode;
    }

    public int getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(int timenumber) {
        this.timenumber = timenumber;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getLisenceplates() {
        return lisenceplates;
    }

    public void setLisenceplates(String lisenceplates) {
        this.lisenceplates = lisenceplates;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public boolean isWalletparking() {
        return walletparking;
    }

    public void setWalletparking(boolean walletparking) {
        this.walletparking = walletparking;
    }

}
