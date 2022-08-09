package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ParkingHistoryRes implements Serializable {
    private Integer id;
    private LocalDateTime starttime;
    private Integer timenumber;
    private String carname;
    private String lisenceplates;
    private String parkingname;

    public ParkingHistoryRes() {
    }

    public ParkingHistoryRes(Integer id, LocalDateTime starttime, Integer timenumber, String carname, String lisenceplates, String parkingname) {
        this.id = id;
        this.starttime = starttime;
        this.timenumber = timenumber;
        this.carname = carname;
        this.lisenceplates = lisenceplates;
        this.parkingname = parkingname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = LocalDateTime.parse(starttime);
    }

    public Integer getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(Integer timenumber) {
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
}
