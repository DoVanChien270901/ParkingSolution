package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.request.TransactionReq;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class RechargeTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;
    private TransactionReq transactionReq;

    public RechargeTask() {
    }

    public RechargeTask(Activity activity,TransactionReq transactionReq) {
        this.activity = activity;
        this.transactionReq= transactionReq;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        LoginRes loginRes = (LoginRes) Session.getSession();
        String uri = activity.getString(R.string.URL_BASE);

        //transaction
        transactionReq.setUsername(loginRes.getUsername());
        HttpEntity request = RestTemplateConfiguration.setRequest(transactionReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "createTransaction", HttpMethod.POST, request, String.class);

        return response;
    }
}
