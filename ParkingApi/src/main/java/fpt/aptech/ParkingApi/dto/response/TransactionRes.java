/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.dto.response;

import java.time.LocalDateTime;

/**
 *
 * @author PCvinhvizg
 */
public class TransactionRes {

    private String transno;
    private Double amount;
    private String channel;
    private LocalDateTime datetime;
    private Integer statuscode;

    public TransactionRes() {
    }

    public String getTransNo() {
        return transno;
    }

    public void setTransNo(String transNo) {
        this.transno = transNo;
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

    public LocalDateTime getDateTime() {
        return datetime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.datetime = dateTime;
    }

    public Integer getStatusCode() {
        return statuscode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statuscode = statusCode;
    }

}
