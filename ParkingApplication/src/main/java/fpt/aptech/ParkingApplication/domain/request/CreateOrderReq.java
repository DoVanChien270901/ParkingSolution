/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.domain.request;

import fpt.aptech.ParkingApplication.domain.response.PaymentChannel;

/**
 *
 * @author PCvinhvizg
 */
public class CreateOrderReq {
    private String username;
    private String transno;
    private PaymentChannel channel;
    private Long amount;
    private String stype;

    public CreateOrderReq() {
    }

    public String getUsername() {
        return username;
    }

    public String getTransNo() {
        return transno;
    }

    public void setTransNo(String transNo) {
        this.transno = transNo;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return stype;
    }

    public void setTransType(String transType) {
        this.stype = transType;
    }
    
    
}
