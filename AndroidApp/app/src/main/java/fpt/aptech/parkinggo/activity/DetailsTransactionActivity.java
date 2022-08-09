package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.domain.response.TransactionRes;

public class DetailsTransactionActivity extends AppCompatActivity {
private TextInputLayout etTransno, etParkingName, etDateTime, etAmount, etChannel , etType, etStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_transaction);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Details Transaction History");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //find element
        etTransno = findViewById(R.id.a_details_transaction_et_stranno);
        etParkingName = findViewById(R.id.a_details_transaction_et_parkingname);
        etDateTime = findViewById(R.id.a_details_transaction_et_datetime);
        etAmount = findViewById(R.id.a_details_transaction_et_amount);
        etChannel = findViewById(R.id.a_details_transaction_et_channel);
        etType = findViewById(R.id.a_details_transaction_et_type);
        etStatus = findViewById(R.id.a_details_transaction_et_status);
        //set text
        TransactionRes item = (TransactionRes) getIntent().getSerializableExtra("item");
        etTransno.getEditText().setText(item.getTransno());
        etParkingName.getEditText().setText(item.getParkingname());
        etType.getEditText().setText(item.getStype());
        etDateTime.getEditText().setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(item.getDatetime()));
        etTransno.getEditText().setText(item.getTransno());
        if(item.getStype().equals("e-Booking")){
            etAmount.getEditText().setText("- "+formatInteger(item.getAmount().toString())+"đ");
            etAmount.getEditText().setTextColor(Color.parseColor("#ff5722"));
            return;
        }else{
            etAmount.getEditText().setText("+ "+formatInteger(item.getAmount().toString())+"đ");
            etAmount.getEditText().setTextColor(Color.parseColor("#3C8100"));
            return;
        }
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }
}