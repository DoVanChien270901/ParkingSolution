package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.CreateBookingTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BookingActivity extends AppCompatActivity {

    TextView parkingname;

    private EditText etDatetime;

    private Button btnBook;

    EPaymentRes bookingRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

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

        //Booking
        EditText etTimenum = findViewById(R.id.a_booking_et_timenum);
        EditText etCarname = findViewById(R.id.a_booking_et_carname);
        EditText etLisenceplates = findViewById(R.id.a_booking_et_lisenceplates);
        EditText etStarttime = findViewById(R.id.a_booking_et_date);

        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBookingTask CreateBookingTask = new CreateBookingTask(BookingActivity.this);
                try {
                    ResponseEntity<?> res = CreateBookingTask.execute().get();
                    bookingRes = (EPaymentRes) res.getBody();
                    Intent transIntent = new Intent(BookingActivity.this, PaymentActivity.class);
                    intent.putExtra("bookingRes", bookingRes);

                    ArrayList bookingReq = new ArrayList();
                    bookingReq.add(etTimenum.getText().toString());
                    bookingReq.add(etCarname.getText().toString());
                    bookingReq.add(etLisenceplates.getText().toString());
                    bookingReq.add(etStarttime.getText().toString());

                    intent.putExtra("bookingReq", bookingReq);
                    startActivity(transIntent);

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        BookingActivity.this.etDatetime.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(BookingActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(BookingActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}