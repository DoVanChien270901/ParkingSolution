package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.CreatePaymentTask;
import fpt.aptech.parkinggo.asynctask.LoadStatusParkingTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoadStatusParking;

public class BookingActivity extends AppCompatActivity {

    TextView parkingname;

    private EditText etDatetime;
    private EditText etCarname;
    private EditText etLisenceplates;
    private EditText etTimenumber;

    private Button btnBook;
    private Button btnselectLocation;
    private RadioButton rbMomo;
    private RadioButton rbZalopay;
    private RadioButton rbWallet;
    private String message;

    private TableLayout tb;

    EPaymentRes bookingRes = null;

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
        message = intent.getStringExtra("parkingname");
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

        btnselectLocation = findViewById(R.id.a_booking_btn_choselocation);
        btnselectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadDialog();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Booking
        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePaymentTask CreatePaymentTask = new CreatePaymentTask(BookingActivity.this);
                try {
                    ResponseEntity<?> res = CreatePaymentTask.execute().get();
                    bookingRes = (EPaymentRes) res.getBody();
                    Intent Payintent = new Intent(BookingActivity.this, PaymentActivity.class);
                    Payintent.putExtra("bookingRes", bookingRes);

                    //booking
                    EditText etTimenum = findViewById(R.id.a_booking_et_timenum);
                    EditText etCarname = findViewById(R.id.a_booking_et_carname);
                    EditText etLisenceplates = findViewById(R.id.a_booking_et_lisenceplates);
                    EditText etStarttime = findViewById(R.id.a_booking_et_date);
                    EditText etLocationCode = findViewById(R.id.a_booking_et_locationcode);

                    String[] car = {etTimenum.getText().toString(), etCarname.getText().toString(), etLisenceplates.getText().toString(), etStarttime.getText().toString(), etLocationCode.getText().toString()};

                    Payintent.putExtra("car", car);

                    startActivity(Payintent);
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

    public void loadDialog() throws ExecutionException, InterruptedException {
        LoadStatusParkingTask loadStatusParkingTask = new LoadStatusParkingTask(this, message);
        LoadStatusParking loadStatusParking = null;
        try {
            ResponseEntity<?> response = loadStatusParkingTask.execute().get();
            loadStatusParking = (LoadStatusParking) response.getBody();
        } catch (Exception e) {
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View alertView = inflater.inflate(R.layout.table_select_location_dialog, null);
        TableLayout tableLayout = alertView.findViewById(R.id.dialog_table);
        TableRow row = new TableRow(this);
        String selected = "A";
        int call = 0;
        for (int i = 0; i < loadStatusParking.getLocationcode().length; i++) {
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView textView = new TextView(this);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(loadStatusParking.getLocationcode()[i]);
            for (int j = 0; j < loadStatusParking.getCodebooked().length; j++) {
                if (loadStatusParking.getLocationcode()[i].equals(loadStatusParking.getCodebooked()[j])) {
                    call = 1;
                }
            }
            if (call == 0) {
                textView.setTextColor(Color.parseColor("#08FF00"));
                textView.setBackground(getDrawable(R.drawable.border_item_table_row_green));
            } else if (call == 1) {
                textView.setTextColor(Color.parseColor("#FF0000"));
                textView.setBackground(getDrawable(R.drawable.border_item_table_row_red));
                call = 0;
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTextView(textView, alertView);
                }
            });
            textView.setTextSize(12);
            textView.setWidth(70);
            textView.setHeight(70);
//            textView.setPadding(15, 15 ,15 ,15);
            row.addView(textView);
            if ((i + 1) % loadStatusParking.getColumnofrow() == 0) {
                tableLayout.addView(row);
                row = new TableRow(this);
            }
        }
        builder.setView(alertView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btnOK = alertView.findViewById(R.id.dialog_btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvLocationCode = findViewById(R.id.a_booking_et_locationcode);
                tvLocationCode.setText(textSelected);
                alertDialog.dismiss();
            }
        });

    }

    TextView selected = null;
    ColorStateList color;
    Drawable background;
    String textSelected;

    public void onClickTextView(TextView tv, View v) {
        if (tv.getTextColors().getDefaultColor() == ((ColorStateList) ColorStateList.valueOf(Color.parseColor("#FF0000"))).getDefaultColor()) {
            return;
        }
        if (selected == null) {
            selected = tv;
            color = tv.getTextColors();
            background = tv.getBackground();
            tv.setTextColor(Color.parseColor("#2196F3"));
            tv.setBackground(getDrawable(R.drawable.border_item_table_row_blue));
            textSelected = tv.getText().toString();
            return;
        }
        if (selected != null) {
            selected.setTextColor(color);
            selected.setBackground(background);
            selected = tv;
            tv.setTextColor(Color.parseColor("#2196F3"));
            tv.setBackground(getDrawable(R.drawable.border_item_table_row_blue));
            textSelected = tv.getText().toString();
            return;
        }
    }
}