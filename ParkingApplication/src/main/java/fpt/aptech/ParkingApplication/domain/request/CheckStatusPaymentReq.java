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
public class CheckStatusPaymentReq {

    private String transno;
    private PaymentChannel channel;

    public CheckStatusPaymentReq() {
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

}
