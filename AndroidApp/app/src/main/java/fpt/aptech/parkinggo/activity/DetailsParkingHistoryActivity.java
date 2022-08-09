package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.ParkingHistoryRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;

public class DetailsParkingHistoryActivity extends AppCompatActivity {
    private TextInputLayout etParkingName, etDateTime, etCarName, etLicensePlates, etToDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_parking_history);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Detail Parking History");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //find all element
        etParkingName = findViewById(R.id.a_details_parking_et_parkingname);
        etDateTime = findViewById(R.id.a_details_parking_et_fromdate);
        etToDate = findViewById(R.id.a_details_parking_et_todate);
        etCarName = findViewById(R.id.a_details_parking_et_carname);
        etLicensePlates = findViewById(R.id.a_details_parking_et_license_plates);
        ParkingHistoryRes item = (ParkingHistoryRes) getIntent().getSerializableExtra("item");
        etParkingName.getEditText().setText(item.getParkingname());
        int month = item.getStarttime().getMonthValue()+ item.getTimenumber();
        LocalDateTime todate = item.getStarttime().plusHours(month);
        etDateTime.getEditText().setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(item.getStarttime()));
        etToDate.getEditText().setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(todate));
        etCarName.getEditText().setText(item.getCarname());
        etLicensePlates.getEditText().setText(item.getLisenceplates());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }
}