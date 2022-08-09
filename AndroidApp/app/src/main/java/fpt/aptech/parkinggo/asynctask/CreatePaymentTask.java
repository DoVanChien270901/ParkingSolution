package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.EBookingReq;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;

public class CreatePaymentTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    private Activity activity;

    public CreatePaymentTask() {
    }

    public CreatePaymentTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        //EditText
        TextView parkingname = activity.findViewById(R.id.a_booking_et_parkingname);
        EditText etTimenumber = activity.findViewById(R.id.a_booking_et_timenum);

        //RadioGroup
        RadioButton rbMomo = activity.findViewById(R.id.a_booking_rb_momo);
        RadioButton rbZalopay = activity.findViewById(R.id.a_booking_rb_zalopay);
        RadioButton rbWallet = activity.findViewById(R.id.a_booking_rb_wallet);

        String channel = null;
        if (rbMomo.isChecked()) {
            channel = "Momo";
        } else if (rbZalopay.isChecked()) {
            channel = "Zalopay";
        }

        Integer timenumber = Integer.parseInt(etTimenumber.getText().toString());
        EBookingReq eBookingReq = new EBookingReqBuilder()
                .setParkingname(parkingname.getText().toString())
                .setChannel(channel)
                .setTimenumber(timenumber)
                .createBookEReq();
        HttpEntity request = RestTemplateConfiguration.setRequest(eBookingReq);
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "e-booking", HttpMethod.POST, request, EPaymentRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {}
}
