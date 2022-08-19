package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.request.ERechargeReq;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;

public class CreateRechargeTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    private Activity activity;

    public CreateRechargeTask() {
    }

    public CreateRechargeTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        TextInputLayout etAmount = activity.findViewById(R.id.a_epayment_et_amount);
        Long amount = Long.parseLong(etAmount.getEditText().getText().toString().replace("đ", "").replaceAll("[,]", ""));;
        //Long amount = Long.parseLong(etAmount.getText().toString());

        //RadioGroup
        RadioButton rbMomo = activity.findViewById(R.id.a_recharge_rb_momo);
        RadioButton rbZalopay = activity.findViewById(R.id.a_recharge_rb_zalopay);

        String channel = null;
        if (rbMomo.isChecked()) {
            channel = "Momo";
        } else if (rbZalopay.isChecked()) {
            channel = "Zalopay";
        }

        ERechargeReq rechargeReq = new ERechargeReq();
        rechargeReq.setChannel(channel);
        rechargeReq.setAmount(amount);

        String uri = activity.getString(R.string.URL_BASE);
        HttpEntity request = RestTemplateConfiguration.setRequest(rechargeReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "e-recharge", HttpMethod.POST, request, EPaymentRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
    }
}
