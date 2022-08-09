package fpt.aptech.parkinggo.domain.response;

import java.io.Serializable;

import fpt.aptech.parkinggo.domain.request.TransactionReq;

public class EPaymentRes implements Serializable {
    private String payUrl;
    private String returnMessage;
    private String signature;
    private String transNo;
    private Integer returnCode;
    private TransactionReq transactionReq;

    public EPaymentRes() {
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public TransactionReq getTransactionReq() {
        return transactionReq;
    }

    public void setTransactionReq(TransactionReq transactionReq) {
        this.transactionReq = transactionReq;
    }
}
