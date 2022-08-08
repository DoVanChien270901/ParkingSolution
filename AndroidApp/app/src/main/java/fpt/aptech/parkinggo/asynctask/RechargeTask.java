package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Text;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class RechargeTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;
    private EPaymentRes rechargeRes;

    public RechargeTask() {
    }

    public RechargeTask(Activity activity, EPaymentRes rechargeRes) {
        this.activity = activity;
        this.rechargeRes = rechargeRes;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        LoginRes loginRes = (LoginRes) Session.getSession();
        String uri = activity.getString(R.string.URL_BASE);

        //transaction
        rechargeRes.getTransactionReq().setUsername(loginRes.getUsername());
        HttpEntity request = RestTemplateConfiguration.setRequest(rechargeRes.getTransactionReq());
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "createTransaction", HttpMethod.POST, request, String.class);

        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
        if (response.getStatusCode().equals(HttpStatus.OK)){
            TextView tvTransno = activity.findViewById(R.id.a_payment_tv_transno);
            TextView tvAmount = activity.findViewById(R.id.a_payment_tv_amount);
            TextView tvChannel = activity.findViewById(R.id.a_payment_tv_channel);
            TextView tvStype = activity.findViewById(R.id.a_payment_tv_stype);
            TextView tvStatus = activity.findViewById(R.id.a_payment_tv_status);

            tvStatus.setText("Payment Successful");
            tvTransno.setText(rechargeRes.getTransNo());
            tvAmount.setText(rechargeRes.getTransactionReq().getAmount().toString());
            tvChannel.setText(rechargeRes.getTransactionReq().getPaymentReq().getChannel());
            tvStype.setText(rechargeRes.getTransactionReq().getStype());
        }else{

        }
    }
}
