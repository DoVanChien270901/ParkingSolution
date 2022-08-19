package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.CreatePaymentTask;
import fpt.aptech.parkinggo.asynctask.LoadStatusParkingTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoadStatusParking;

public class BookingActivity extends AppCompatActivity {

    private TextView parkingname, tvPrice;

//    private EditText etCarname;
//    private EditText etLisenceplates;
//    private EditText etTimenumber;

    private Button btnBook;
    private Button btnselectLocation;
    private RadioButton rbMomo;
    private RadioButton rbZalopay;
    private RadioButton rbWallet;
    private String message;
    private Double rentCost;
    private TableLayout tb;

    EPaymentRes bookingRes = null;
    private TextInputLayout etCarname, etLisenceplates, etTimenumber, etLocationCode, etDatetime;
    private AutoCompleteTextView actDelayTime, actLocationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Booking");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //EditText
        etCarname = findViewById(R.id.a_booking_et_carname);
        tvPrice = findViewById(R.id.a_booking_tv_price);
        etLisenceplates = findViewById(R.id.a_booking_et_lisenceplates);
        etLocationCode = findViewById(R.id.a_booking_et_locationcode);
        etTimenumber = findViewById(R.id.a_booking_et_timenum);
        actDelayTime = findViewById(R.id.a_booking_act_date);
        actLocationCode = findViewById(R.id.a_booking_act_locationcode);
        rbMomo = findViewById(R.id.a_booking_rb_momo);
        rbZalopay = findViewById(R.id.a_booking_rb_zalopay);
        rbWallet = findViewById(R.id.a_booking_rb_wallet);

        //get Parking Name
        Intent intent = getIntent();
        message = intent.getStringExtra("parkingname");
        rentCost = intent.getDoubleExtra("rentCost", 0);
        parkingname = findViewById(R.id.a_booking_et_parkingname);
        parkingname.setText(message);
        //DateTime Picker
        etDatetime = findViewById(R.id.a_booking_et_date);
        //set value delay time
        LocalDateTime lcCdurrentDate = LocalDateTime.now();
        String []option = new String[31];
        for(int i = 0; i<31; i ++){
            option[i] = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(lcCdurrentDate.plusMinutes(i));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, option);
        actDelayTime.setAdapter(arrayAdapter);
        //validate
        //car name
        etCarname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateCarName();
            }
        });
        //license plates
        etLisenceplates.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateLicensePlates();
            }
        });
        //time number
        etTimenumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Integer i = (Integer.valueOf(s.toString()))*rentCost.intValue();
                    tvPrice.setText("The amount to be paid is: "+formatInteger(String.valueOf(i))+"đ");
                }catch (Exception e){
                    tvPrice.setText("The amount to be paid is: 0đ");
                }
                validateTimeNumber();
            }
        });
        //validateTimeOfPresence
        actLocationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadDialog();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //submit
        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateCarName()&&validateLicensePlates()&&validateTimeNumber()&&validateTimeOfPresence()&&validateSelectLocation()&&validatePaymentMethod()){
                    CreatePaymentTask CreatePaymentTask = new CreatePaymentTask(BookingActivity.this);
                    if (rbWallet.isChecked()){
                        try {
                            CreatePaymentTask.execute();
                            startActivity(new Intent(BookingActivity.this, BookingHistoryActivity.class));
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        ResponseEntity<?> res = CreatePaymentTask.execute().get();
                        bookingRes = (EPaymentRes) res.getBody();
                        Intent Payintent = new Intent(BookingActivity.this, PaymentActivity.class);
                        Payintent.putExtra("bookingRes", bookingRes);
                        String[] car = {etTimenumber.getEditText().getText().toString(), etCarname.getEditText().getText().toString(), etLisenceplates.getEditText().getText().toString(), actLocationCode.getText().toString(), actDelayTime.getText().toString(), etLisenceplates.getEditText().getText().toString()};

                        Payintent.putExtra("car", car);

                        startActivity(Payintent);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    return;
                }

            }
        });
    }

    public void loadDialog() throws ExecutionException, InterruptedException {
        LoadStatusParkingTask loadStatusParkingTask = new LoadStatusParkingTask(this, message);
        LoadStatusParking loadStatusParking = null;
        try {
            ResponseEntity<?> response = loadStatusParkingTask.execute().get();
            loadStatusParking = (LoadStatusParking) response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
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
//                TextView tvLocationCode = findViewById(R.id.a_booking_et_locationcode);
                actLocationCode.setText(textSelected);
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
    private boolean validateCarName() {
        if(etCarname.getEditText().getText().length()<1){
            etCarname.setError("Car name is required");
            return false;
        } else {
            etCarname.setError(null);
            return true;
        }
    }
    private boolean validateLicensePlates() {
        if(etLisenceplates.getEditText().getText().length()<1){
            etLisenceplates.setError("License plates is required");
            return false;
        } else {
            etLisenceplates.setError(null);
            return true;
        }
    }
    private boolean validateTimeNumber() {
        if(etTimenumber.getEditText().getText().length()<1){
            etTimenumber.setError("Select the number of hours to book");
            return false;
        }
        else if(Integer.valueOf(etTimenumber.getEditText().getText().toString()) > 24){
            etTimenumber.setError("Maximum time is twenty-four hours");
            return false;
        } else {
            etTimenumber.setError(null);
            return true;
        }
    }
    private boolean validateTimeOfPresence() {
        if(actDelayTime.getText().length()<1){
            etDatetime.setError("Select the time of presence");
            return false;
        }
        else {
            etDatetime.setError(null);
            return true;
        }
    }
    private boolean validateSelectLocation() {
        if(actLocationCode.getText().length()<1){
            etLocationCode.setError("Select the location in parking");
            return false;
        }
        else {
            etLocationCode.setError(null);
            return true;
        }
    }
    private boolean validatePaymentMethod(){
        TextView textView = findViewById(R.id.a_booking_tv_errorPaymentMethod);
        if (rbMomo.isChecked()||rbWallet.isChecked()||rbZalopay.isChecked()){
            textView.setVisibility(View.GONE);
            return true;
        }else{
            textView.setVisibility(View.VISIBLE);
            return false;
        }
    }
}