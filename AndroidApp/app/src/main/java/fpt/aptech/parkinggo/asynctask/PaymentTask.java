package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.NewBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.NewBookingReq;
import fpt.aptech.parkinggo.domain.request.TransactionReq;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class PaymentTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;
    private EBookingRes bookingRes;

    public PaymentTask() {
    }

    public PaymentTask(Activity activity, EBookingRes bookingRes) {
        this.activity = activity;
        this.bookingRes = bookingRes;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        LoginRes loginRes = (LoginRes) Session.getSession();
        String uri = activity.getString(R.string.URL_BASE);

        bookingRes.getTransactionReq().setUsername(loginRes.getUsername());
        HttpEntity request = RestTemplateConfiguration.setRequest(bookingRes.getTransactionReq());
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "createTransaction", HttpMethod.POST, request, TransactionReq.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
    }
}
