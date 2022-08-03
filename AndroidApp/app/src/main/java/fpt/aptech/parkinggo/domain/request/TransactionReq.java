package fpt.aptech.parkinggo.domain.request;

public class TransactionReq {
    private String username;
    private String parkingname;
    private CheckStatusPaymentReq paymentReq;
    private String stype;
    private Long amount;

    public TransactionReq() {
    }

    public TransactionReq(String username, String parkingname, CheckStatusPaymentReq paymentReq, String stype, Long amount) {
        this.username = username;
        this.parkingname = parkingname;
        this.paymentReq = paymentReq;
        this.stype = stype;
        this.amount = amount;
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
