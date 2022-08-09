package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;
import fpt.aptech.parkinggo.statics.Session;

public class SearchTransactionHistoryTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;
    private CustomProgressDialog dialog;

    public SearchTransactionHistoryTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog = new CustomProgressDialog(activity);
        dialog.show();
    }
    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        String fromDate = ((TextView) activity.findViewById(R.id.a_transaction_history_tv_fromdate)).getText().toString();
        String toDate = ((TextView) activity.findViewById(R.id.a_transaction_history_tv_todate)).getText().toString();
        String token = ((LoginRes) Session.getSession()).getToken();
        String uri = activity.getString(R.string.URL_BASE);
        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri+"list-all-transaction/search?from-date="+fromDate+"&to-date="+toDate, HttpMethod.GET, request, TransactionRes[].class);
        return response;
    }

     @Override
    protected void onPostExecute(ResponseEntity<?> responseEntity) {
        dialog.dismiss();
    }
}
