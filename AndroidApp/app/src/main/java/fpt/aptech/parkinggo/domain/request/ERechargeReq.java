package fpt.aptech.parkinggo.domain.request;

public class ERechargeReq {
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

