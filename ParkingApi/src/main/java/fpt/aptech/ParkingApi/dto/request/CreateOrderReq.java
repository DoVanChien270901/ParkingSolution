/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.dto.request;

import fpt.aptech.ParkingApi.dto.enumm.PaymentChannel;

/**
 *
 * @author PCvinhvizg
 */
public class CreateOrderReq {
    private String user_name;
    private String transNo;
    private PaymentChannel channel;
    private Long amount;
    private String transType;

    public CreateOrderReq() {
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
    
    
}
