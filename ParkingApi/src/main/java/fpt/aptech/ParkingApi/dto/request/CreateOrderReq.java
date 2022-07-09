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

    private String transno;
    private PaymentChannel channel;
    private Long amount;

    public CreateOrderReq() {
    }

    public String getTransNo() {
        return transno;
    }

    public void setTransNo(String transNo) {
        this.transno = transNo;
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

}
