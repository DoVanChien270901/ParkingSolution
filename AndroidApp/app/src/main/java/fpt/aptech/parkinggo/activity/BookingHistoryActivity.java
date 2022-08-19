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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.adapter.BookingHistoryAdapter;
import fpt.aptech.parkinggo.asynctask.LoadListBookingTask;
import fpt.aptech.parkinggo.domain.response.BookingRes;

public class BookingHistoryActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<BookingRes> bookingRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Booking");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        listView = findViewById(R.id.a_bookinglist_lv_bookinglist);

        LoadListBookingTask bookingTask = new LoadListBookingTask(BookingHistoryActivity.this);
        try {
            ResponseEntity<?> response = bookingTask.execute().get();
            BookingRes[] res = (BookingRes[]) response.getBody();
            bookingRes = new ArrayList<BookingRes>(Arrays.asList(res));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        BookingHistoryAdapter adapter = new BookingHistoryAdapter(bookingRes,BookingHistoryActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(BookingHistoryActivity.this, BookingDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(this, MapsActivity.class);
        startActivity(myIntent);
        return true;
    }

}