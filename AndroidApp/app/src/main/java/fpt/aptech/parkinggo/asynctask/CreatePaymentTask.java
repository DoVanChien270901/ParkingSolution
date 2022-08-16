package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EBookingReqBuilder;
import fpt.aptech.parkinggo.domain.modelbuilder.NewBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.EBookingReq;
import fpt.aptech.parkinggo.domain.request.NewBookingReq;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

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
        TextInputLayout etTimenumber = activity.findViewById(R.id.a_booking_et_timenum);

        //RadioGroup
        RadioButton rbMomo = activity.findViewById(R.id.a_booking_rb_momo);
        RadioButton rbZalopay = activity.findViewById(R.id.a_booking_rb_zalopay);
        RadioButton rbWallet = activity.findViewById(R.id.a_booking_rb_wallet);

        LoginRes loginRes = (LoginRes) Session.getSession();
        String uri = activity.getString(R.string.URL_BASE);
        String channel = null;
        if (rbMomo.isChecked()) {
            channel = "Momo";
        } else if (rbZalopay.isChecked()) {
            channel = "Zalopay";
        }else if (rbWallet.isChecked()){
            TextView tvParkingName = activity.findViewById(R.id.a_booking_et_parkingname);
            EditText etCarname = ((TextInputLayout) activity.findViewById(R.id.a_booking_et_carname)).getEditText();
            EditText etLisenceplates = ((TextInputLayout) activity.findViewById(R.id.a_booking_et_lisenceplates)).getEditText();
            EditText etTimeNumber1 = ((TextInputLayout)activity.findViewById(R.id.a_booking_et_timenum)).getEditText();
            AutoCompleteTextView actDate = activity.findViewById(R.id.a_booking_act_date);
            AutoCompleteTextView actLocationCode = activity.findViewById(R.id.a_booking_act_locationcode);
            NewBookingReq newBookingReq = new NewBookingReqBuilder()
                    .setParkingname(tvParkingName.getText().toString())
                    .setUsername(loginRes.getUsername())
                    .setTimenumber(Integer.parseInt(etTimeNumber1.getText().toString()))
                    .setCarname(etCarname.getText().toString())
                    .setLisenceplates(etLisenceplates.getText().toString())
                    .setStarttime(actDate.getText().toString())
                    .setWalletparking(true)
                    .createBookEReq();
            newBookingReq.setLocationcode(actLocationCode.getText().toString());

            HttpEntity request = RestTemplateConfiguration.setRequest(newBookingReq);
            ResponseEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(uri + "booking", HttpMethod.POST, request, Integer.class);
            return null;
        }

        Integer timenumber = Integer.parseInt(etTimenumber.getEditText().getText().toString());
        EBookingReq eBookingReq = new EBookingReqBuilder()
                .setParkingname(parkingname.getText().toString())
                .setChannel(channel)
                .setTimenumber(timenumber)
                .createBookEReq();
        HttpEntity request = RestTemplateConfiguration.setRequest(eBookingReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "e-booking", HttpMethod.POST, request, EPaymentRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {}
}
