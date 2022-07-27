package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.activity.BookingActivity;
import fpt.aptech.parkinggo.activity.HomeActivity;
import fpt.aptech.parkinggo.activity.LoginActivity;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.EBookingReq;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BookingTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    private Activity activity;

    public BookingTask() {
    }

    public BookingTask(Activity activity) {
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
                .excuteRequest(uri + "e-booking", HttpMethod.POST, request, EBookingRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {}
}
