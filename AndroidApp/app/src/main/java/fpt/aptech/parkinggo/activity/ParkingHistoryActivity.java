package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.adapter.ParkingHistoryAdapter;
import fpt.aptech.parkinggo.adapter.TransactionHistoryAdapter;
import fpt.aptech.parkinggo.asynctask.ParkingHistoryTask;
import fpt.aptech.parkinggo.asynctask.SearchParkingHistoryTask;
import fpt.aptech.parkinggo.asynctask.SearchTransactionHistoryTask;
import fpt.aptech.parkinggo.asynctask.TransactionHistoryTask;
import fpt.aptech.parkinggo.domain.response.ParkingHistoryRes;
import fpt.aptech.parkinggo.domain.response.TransactionRes;

public class ParkingHistoryActivity extends AppCompatActivity {
    private ParkingHistoryRes[] parkingHistoryRes;
    private TextView tvFromDate, tvToDate;
    private LocalDate clFromDate, clToDate;
    private ImageButton imbSearch;
    private String sToDate, sFromDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Parking History");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //find all element
        tvFromDate = findViewById(R.id.a_parking_history_tv_fromdate);
        tvToDate = findViewById(R.id.a_parking_history_tv_todate);
        imbSearch = findViewById(R.id.a_parking_history_imb_search);
        //fromDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        sFromDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(((LocalDate)LocalDate.now()).withMonth(((LocalDate) LocalDate.now()).getMonthValue()-1));
        tvFromDate.setText(sFromDate);
        clFromDate = LocalDate.parse(sFromDate, formatter);
        //toDate
        sToDate = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());
        tvToDate.setText(sToDate);
        clToDate = LocalDate.parse(tvToDate.getText(), formatter);
        //click item
        ListView listView = (ListView) findViewById(R.id.a_parking_history_lv);
        //onclick item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParkingHistoryRes item = parkingHistoryRes[position];
                Intent intent = new Intent(ParkingHistoryActivity.this, DetailsParkingHistoryActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });
        //click tv date
        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ParkingHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sFromDate = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
                        tvFromDate.setText(sFromDate);
                        clFromDate = LocalDate.parse(sFromDate, formatter);
//                        if (month < 10||dayOfMonth<10) {
//                            sFromDate = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
//                            tvFromDate.setText(sFromDate);
//                            clFromDate = LocalDate.parse(sFromDate, formatter);
//                        } else {
//                            sFromDate = dayOfMonth + "-" + (month + 1) + "-" + year;
//                            tvFromDate.setText(sFromDate);
//                            clFromDate = LocalDate.parse(sFromDate, formatter);
//                        }
                    }
                }, clFromDate.getYear(), clFromDate.getMonthValue()-1, clFromDate.getDayOfMonth());
                datePickerDialog.show();
            }
        });
        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ParkingHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sToDate = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
                        tvToDate.setText(sToDate);
                        clToDate = LocalDate.parse(sToDate, formatter);
//                        if (month < 10||dayOfMonth<10) {
//                            sToDate = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
//                            tvToDate.setText(sToDate);
//                            clToDate = LocalDate.parse(sToDate, formatter);
//                        } else {
//                            sToDate = dayOfMonth + "-" + (month + 1) + "-" + year;
//                            tvToDate.setText(sToDate);
//                            clToDate = LocalDate.parse(sToDate, formatter);
//                        }
                    }
                }, clToDate.getYear(), clToDate.getMonthValue()-1, clToDate.getDayOfMonth());
                datePickerDialog.show();
            }
        });
        //
        loadListView();
        //submit search
        imbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }
    public void loadListView(){
        try {
            ResponseEntity response = ((ParkingHistoryTask) new ParkingHistoryTask(this)).execute().get();
            parkingHistoryRes = (ParkingHistoryRes[]) response.getBody();
        }catch (Exception e){
        }
        ListView listView = (ListView) findViewById(R.id.a_parking_history_lv);
        ParkingHistoryAdapter transactionHistoryAdapter
                = new ParkingHistoryAdapter(this,parkingHistoryRes);
        listView.setAdapter(transactionHistoryAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }
    public void search(){
        try {
            ResponseEntity<?> response = ((SearchParkingHistoryTask) new SearchParkingHistoryTask(this)).execute().get();
            if (((ParkingHistoryRes[]) response.getBody()).length>=1){
                parkingHistoryRes = (ParkingHistoryRes[]) response.getBody();
                ListView listView = (ListView) findViewById(R.id.a_parking_history_lv);
                ParkingHistoryAdapter transactionHistoryAdapter
                        = new ParkingHistoryAdapter(this, parkingHistoryRes);
                listView.setAdapter(transactionHistoryAdapter);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = LayoutInflater.from(this);
                View alertView = inflater.inflate(R.layout.error_dialog, null);
                TextView tvContent = alertView.findViewById(R.id.error_dialog_tv_content);
                tvContent.setText("The account has no parking history during this time!");
                builder.setView(alertView);
                final Dialog er = builder.show();
                Button btnError = alertView.findViewById(R.id.error_dialog_btn_retry);
                btnError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        er.dismiss();
                    }
                });
                //Toast.makeText(this, "The account has no parking history during this time", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
//            ListView listView = (ListView) findViewById(R.id.a_transaction_history_lv);
//            listView.setAdapter(null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View alertView = inflater.inflate(R.layout.error_dialog, null);
            TextView tvContent = alertView.findViewById(R.id.error_dialog_tv_content);
            tvContent.setText("The account has no parking history during this time!");
            builder.setView(alertView);
            final Dialog er = builder.show();
            Button btnError = alertView.findViewById(R.id.error_dialog_btn_retry);
            btnError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    er.dismiss();
                }
            });
            //Toast.makeText(this, "The account has no parking history during this time", Toast.LENGTH_LONG).show();
        }
    }
}