package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingDetailTask;
import fpt.aptech.parkinggo.domain.response.BookingDetailRes;

public class BookingDetailActivity extends AppCompatActivity {

    private BookingDetailRes bookingDetailRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Detail Booking");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        long id = (long) getIntent().getSerializableExtra("id");

        BookingDetailTask bookingDetailTask = new BookingDetailTask(this, id);
        try {
            HttpEntity<?> response = bookingDetailTask.execute().get();
            bookingDetailRes = (BookingDetailRes) response.getBody();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        EditText etParkingname = ((TextInputLayout)findViewById(R.id.a_details_booking_et_parkingname)).getEditText();
        EditText etStarttime = ((TextInputLayout)findViewById(R.id.a_details_booking_et_starttime)).getEditText();
        EditText etEndTime = ((TextInputLayout)findViewById(R.id.a_details_booking_et_endtime)).getEditText();
        EditText etPrice = ((TextInputLayout)findViewById(R.id.a_details_booking_et_price)).getEditText();
        EditText etCarname = ((TextInputLayout)findViewById(R.id.a_details_booking_et_carname)).getEditText();
        EditText etLisenceplates = ((TextInputLayout)findViewById(R.id.a_details_booking_et_lisenceplates)).getEditText();
        EditText etLocationCode = ((TextInputLayout)findViewById(R.id.a_details_booking_et_location_code)).getEditText();


        etParkingname.setText(bookingDetailRes.getParkingname());
        etStarttime.setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(bookingDetailRes.getStarttime()));
        etEndTime.setText(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy").format(bookingDetailRes.getStarttime().plusHours(bookingDetailRes.getStarttime().getMonthValue()+ bookingDetailRes.getTimenumber())));
        etPrice.setText(formatInteger(String.valueOf(bookingDetailRes.getPrice()))+"đ");
        etCarname.setText(bookingDetailRes.getCarname());
        etLisenceplates.setText(bookingDetailRes.getLisenceplates());
        etLocationCode.setText(bookingDetailRes.getLocationcode());

        //qrcode
        ImageView imageView = findViewById(R.id.a_details_booking_imv_qrcode);
        byte[] qrcontent = bookingDetailRes.getQrcontent();
        Bitmap bmp = BitmapFactory.decodeByteArray(qrcontent, 0, qrcontent.length);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 550, 550, false));
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
}