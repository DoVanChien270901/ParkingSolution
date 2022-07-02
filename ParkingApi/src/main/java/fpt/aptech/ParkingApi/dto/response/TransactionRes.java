/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.dto.response;

import fpt.aptech.ParkingApi.dto.enumm.PaymentChannel;
import java.time.LocalDateTime;

/**
 *
 * @author PCvinhvizg
 */
public class TransactionRes {

    private String transNo;
    private Double amount;
    private PaymentChannel channel;
    private LocalDateTime dateTime;
//    private String description;
    private Integer statusCode;
//    private String type;

    public TransactionRes() {
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
