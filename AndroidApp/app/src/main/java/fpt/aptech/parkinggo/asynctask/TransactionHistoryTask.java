package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.request.RegisterReq;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;
import fpt.aptech.parkinggo.statics.Session;

public class TransactionHistoryTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;

    public TransactionHistoryTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void...voids) {
        String token = ((LoginRes) Session.getSession()).getToken();
        String uri = activity.getString(R.string.URL_BASE);
        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri+"list-all-transaction", HttpMethod.GET, request, TransactionRes[].class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> responseEntity) {
    }
}
