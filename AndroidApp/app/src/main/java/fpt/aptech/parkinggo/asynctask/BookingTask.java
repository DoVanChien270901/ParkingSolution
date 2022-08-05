package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.NewBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.NewBookingReq;
import fpt.aptech.parkinggo.domain.request.TransactionReq;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class BookingTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    private Activity activity;
    private EBookingRes bookingRes;

    public BookingTask() {
    }

    public BookingTask(Activity activity, EBookingRes bookingRes) {
        this.activity = activity;
        this.bookingRes = bookingRes;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {

        LoginRes loginRes = (LoginRes) Session.getSession();
        String uri = activity.getString(R.string.URL_BASE);

        //transaction
        bookingRes.getTransactionReq().setUsername(loginRes.getUsername());
        HttpEntity request2 = RestTemplateConfiguration.setRequest(bookingRes.getTransactionReq());
        ResponseEntity<?> response2 = RestTemplateConfiguration
                .excuteRequest(uri + "createTransaction", HttpMethod.POST, request2, String.class);
//        TransactionReq transactionReq = (TransactionReq) response2.getBody();

//        //booking
        EditText etTimenum = activity.findViewById(R.id.a_booking_et_timenum);
        EditText etCarname = activity.findViewById(R.id.a_booking_et_carname);
        EditText etLisenceplates = activity.findViewById(R.id.a_booking_et_lisenceplates);
        EditText etStarttime = activity.findViewById(R.id.a_booking_et_date);
        EditText etLocationCode = activity.findViewById(R.id.a_booking_et_locationcode);

        NewBookingReq newBookingReq = new NewBookingReqBuilder()
                .setParkingname(bookingRes.getTransactionReq().getParkingname())
                .setUsername(loginRes.getUsername())
                .setTimenumber(Integer.parseInt(etTimenum.getText().toString()))
                .setCarname(etCarname.getText().toString())
                .setLisenceplates(etLisenceplates.getText().toString())
                .setStarttime(etStarttime.getText().toString())
                .setWalletparking(false)
                .createBookEReq();
        newBookingReq.setLocationcode(etLocationCode.getText().toString());

        HttpEntity request = RestTemplateConfiguration.setRequest(newBookingReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "booking", HttpMethod.POST, request, Integer.class);

        return response;
    }
}
