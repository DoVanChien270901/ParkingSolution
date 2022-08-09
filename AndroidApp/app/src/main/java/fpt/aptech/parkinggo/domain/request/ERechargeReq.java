package fpt.aptech.parkinggo.domain.request;

import java.io.Serializable;

public class ERechargeReq implements Serializable {
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
