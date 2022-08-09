package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.NewBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.NewBookingReq;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class BookingTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {

    private Activity activity;
    private EPaymentRes bookingRes;

    public BookingTask() {
    }

    public BookingTask(Activity activity, EPaymentRes bookingRes) {
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

        //booking
        String[] s = (String[]) activity.getIntent().getSerializableExtra("car");

        NewBookingReq newBookingReq = new NewBookingReqBuilder()
                .setParkingname(bookingRes.getTransactionReq().getParkingname())
                .setUsername(loginRes.getUsername())
                .setTimenumber(Integer.parseInt(s[0]))
                .setCarname(s[1])
                .setLisenceplates(s[2])
                .setStarttime(s[3])
                .setWalletparking(false)
                .createBookEReq();
        newBookingReq.setLocationcode(s[4]);

        HttpEntity request = RestTemplateConfiguration.setRequest(newBookingReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri + "booking", HttpMethod.POST, request, Integer.class);

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
            tvTransno.setText(bookingRes.getTransNo());
            tvAmount.setText(bookingRes.getTransactionReq().getAmount().toString());
            tvChannel.setText(bookingRes.getTransactionReq().getPaymentReq().getChannel());
            tvStype.setText(bookingRes.getTransactionReq().getStype());
        }else{

        }
    }
}
