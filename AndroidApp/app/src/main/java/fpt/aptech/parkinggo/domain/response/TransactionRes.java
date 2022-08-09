package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TransactionRes implements Serializable {
    private String transno;
    private Double amount;
    private String channel;
    private LocalDateTime datetime;
    private String stype;
    private String parkingname;
    private Integer statuscode;

    public TransactionRes() {
    }


    public TransactionRes(String transno, Double amount, String channel, LocalDateTime datetime, String stype, String parkingname, Integer statuscode) {
        this.transno = transno;
        this.amount = amount;
        this.channel = channel;
        this.datetime = datetime;
        this.stype = stype;
        this.parkingname = parkingname;
        this.statuscode = statuscode;
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = LocalDateTime.parse(datetime);
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }
}
