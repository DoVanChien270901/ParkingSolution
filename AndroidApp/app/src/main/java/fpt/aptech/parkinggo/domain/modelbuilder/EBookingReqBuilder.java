package fpt.aptech.parkinggo.domain.modelbuilder;

import fpt.aptech.parkinggo.domain.request.EBookingReq;

public class EBookingReqBuilder {
    private String parkingname;
    private String channel;
    private Integer timenumber;

    public EBookingReqBuilder setParkingname(String parkingname) {
        this.parkingname = parkingname;
        return this;
    }

    public EBookingReqBuilder setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public EBookingReqBuilder setTimenumber(Integer timenumber) {
        this.timenumber = timenumber;
        return this;
    }

    public EBookingReq createBookEReq() {
        return new EBookingReq(parkingname, channel, timenumber);
    }
}
