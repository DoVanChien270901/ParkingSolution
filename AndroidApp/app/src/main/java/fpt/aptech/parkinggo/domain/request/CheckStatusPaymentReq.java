package fpt.aptech.parkinggo.domain.request;

import java.io.Serializable;

public class CheckStatusPaymentReq implements Serializable {
    private String transno;
    private String channel;

    public CheckStatusPaymentReq() {
    }

    public String getTransno() {
        return transno;
    }

    public void setTransno(String transno) {
        this.transno = transno;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
