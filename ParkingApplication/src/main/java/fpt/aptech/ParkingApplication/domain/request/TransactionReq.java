/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.domain.request;

/**
 *
 * @author PCvinhvizg
 */
public class TransactionReq {
    private String username;
    private String parkingname;
    private CheckStatusPaymentReq paymentReq;
    private String stype;
    private Long amount;

    public TransactionReq() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public CheckStatusPaymentReq getPaymentReq() {
        return paymentReq;
    }

    public void setPaymentReq(CheckStatusPaymentReq paymentReq) {
        this.paymentReq = paymentReq;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    
    
}
