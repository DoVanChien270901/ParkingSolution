package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.BookingDetailRes;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class BookingDetailTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    Activity activity;
    long id;

    public BookingDetailTask(Activity activity, long id) {
        this.activity = activity;
        this.id = id;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {

        LoginRes loginRes = (LoginRes) Session.getSession();
        String token = loginRes.getToken();
        int idd = Integer.parseInt(String.valueOf(id));

        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "booking-details?id="+idd, HttpMethod.GET, request, BookingDetailRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
    }
}
