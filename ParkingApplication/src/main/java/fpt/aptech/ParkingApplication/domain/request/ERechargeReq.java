/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.domain.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author PCvinhvizg
 */
public class ERechargeReq {
    @Min(value = 10000, message = "Amount must between 10.000 VND and 5.000.000 VND")
    @Max(value = 5000000, message = "Amount must between 10.000 VND and 5.000.000 VND")
    private Long amount;
    private String channel;

    public ERechargeReq() {
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    
}
