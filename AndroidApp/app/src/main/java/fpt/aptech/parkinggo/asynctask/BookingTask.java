package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.activity.BookingActivity;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.EBookingReq;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
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

        EBookingRes eBookingRes = (EBookingRes) response.getBody();
        String token = eBookingRes.getSignature();

        ZaloPaySDK.getInstance().payOrder((Activity) activity.getApplicationContext(), token, "demozpdk://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(activity.getApplicationContext())
                                .setTitle("Payment Success")
                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                });
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                new AlertDialog.Builder(activity.getApplicationContext())
                        .setTitle("User Cancel Payment")
                        .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }

            @Override
            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                new AlertDialog.Builder(activity.getApplicationContext())
                        .setTitle("Payment Fail")
                        .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }
        });

        return null;
    }
}
