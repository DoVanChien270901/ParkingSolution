/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.dto.request;

/**
 *
 * @author PCvinhvizg
 */
public class OrderReq {
    private String user_name;
    private String transNo;
    private String channel;
    private Long amount;
    private String transType;

    public OrderReq() {
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
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
