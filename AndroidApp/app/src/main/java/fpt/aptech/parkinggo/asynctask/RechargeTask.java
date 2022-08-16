package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
//            TextView tvTransno = activity.findViewById(R.id.a_payment_tv_transno);
//            TextView tvAmount = activity.findViewById(R.id.a_payment_tv_amount);
//            TextView tvChannel = activity.findViewById(R.id.a_payment_tv_channel);
//            TextView tvStype = activity.findViewById(R.id.a_payment_tv_stype);
//
//            tvTransno.setText(rechargeRes.getTransNo());
//            tvAmount.setText(rechargeRes.getTransactionReq().getAmount().toString());
//            tvChannel.setText(rechargeRes.getTransactionReq().getPaymentReq().getChannel());
//            tvStype.setText(rechargeRes.getTransactionReq().getStype());
            //reset balance in session
            ((LoginRes) Session.getSession()).setBalance(((LoginRes) Session.getSession()).getBalance() + rechargeRes.getTransactionReq().getAmount());

            EditText tvTransno = ((TextInputLayout)activity.findViewById(R.id.a_payment_tv_transno)).getEditText();
            EditText tvAmount = ((TextInputLayout)activity.findViewById(R.id.a_payment_tv_amount)).getEditText();
            EditText tvChannel = ((TextInputLayout)activity.findViewById(R.id.a_payment_tv_channel)).getEditText();
            EditText tvStype = ((TextInputLayout)activity.findViewById(R.id.a_payment_tv_stype)).getEditText();
            TextView tvStatus = activity.findViewById(R.id.a_payment_tv_status);

            tvStatus.setText("Payment Successful");
            tvTransno.setText(rechargeRes.getTransNo());
            tvAmount.setText(formatInteger(rechargeRes.getTransactionReq().getAmount().toString())+" đ");
            tvChannel.setText(rechargeRes.getTransactionReq().getPaymentReq().getChannel());
            tvStype.setText(rechargeRes.getTransactionReq().getStype());
        }else{

        }
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
}
