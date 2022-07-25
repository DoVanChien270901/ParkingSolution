package fpt.aptech.parkinggo.domain.request;

public class EBookingReq {
    private String parkingname;
    private String channel;
    private Integer timenumber;

    public EBookingReq() {
    }

    public EBookingReq(String parkingname, String channel, Integer timenumber) {
        this.parkingname = parkingname;
        this.channel = channel;
        this.timenumber = timenumber;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getTimenumber() {
        return timenumber;
    }

    public void setTimenumber(Integer timenumber) {
        this.timenumber = timenumber;
    }
}
