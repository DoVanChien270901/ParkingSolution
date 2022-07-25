package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.LoginTask;

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


        //Booking
        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BookingTask(BookingActivity.this).execute();
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