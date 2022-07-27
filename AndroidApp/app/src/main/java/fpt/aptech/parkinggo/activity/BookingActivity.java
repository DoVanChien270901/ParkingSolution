package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.LoginTask;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EBookingReqBuilder;
import fpt.aptech.parkinggo.domain.request.EBookingReq;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.statics.Session;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BookingActivity extends AppCompatActivity {

    TextView parkingname;

    private EditText etDatetime;
    private EditText etCarname;
    private EditText etLisenceplates;
    private EditText etTimenumber;

    private Button btnBook;
    private RadioButton rbMomo;
    private RadioButton rbZalopay;
    private RadioButton rbWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //EditText
        etCarname = findViewById(R.id.a_booking_et_carname);
        etLisenceplates = findViewById(R.id.a_booking_et_lisenceplates);
        etTimenumber = findViewById(R.id.a_booking_et_timenum);

        //get Parking Name
        Intent intent = getIntent();
        String message = intent.getStringExtra("parkingname");
        parkingname = findViewById(R.id.a_booking_et_parkingname);
        parkingname.setText(message);

        //DateTime Picker
        etDatetime = findViewById(R.id.a_booking_et_date);
        etDatetime.setInputType(InputType.TYPE_NULL);
        etDatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(etDatetime);
            }
        });

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        //Booking
        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingTask BookingTask = new BookingTask(BookingActivity.this);
                EBookingRes bookingRes = null;
                try {
                    ResponseEntity<?> res = BookingTask.execute().get();
                    bookingRes = (EBookingRes) res.getBody();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                ZaloPaySDK.getInstance().payOrder(BookingActivity.this, bookingRes.getSignature(), "parkinggo://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(BookingActivity.this)
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
                        new AlertDialog.Builder(BookingActivity.this)
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
                        new AlertDialog.Builder(BookingActivity.this)
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
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void showDateDialog(EditText etDatetime) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-mm-dd HH:mm");
                        BookingActivity.this.etDatetime.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(BookingActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(BookingActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}