package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.BookingRes;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.ListBookingRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class LoadListBookingTask extends AsyncTask<Void, Void, ResponseEntity<?>> {
    Activity activity;
    public LoadListBookingTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
    }
    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        LoginRes loginRes = (LoginRes) Session.getSession();
        String token = loginRes.getToken();
        String uri = activity.getString(R.string.URL_BASE);

        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "android-list-booking", HttpMethod.GET, request, BookingRes[].class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
    }
}
