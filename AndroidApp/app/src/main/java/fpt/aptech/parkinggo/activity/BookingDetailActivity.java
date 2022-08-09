package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;

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
        EditText etTimenum = ((TextInputLayout)findViewById(R.id.a_details_booking_et_timenumber)).getEditText();
        EditText etPrice = ((TextInputLayout)findViewById(R.id.a_details_booking_et_price)).getEditText();
        EditText etCarname = ((TextInputLayout)findViewById(R.id.a_details_booking_et_carname)).getEditText();
        EditText etLisenceplates = ((TextInputLayout)findViewById(R.id.a_details_booking_et_lisenceplates)).getEditText();


        etParkingname.setText(bookingDetailRes.getParkingname());
        etStarttime.setText(String.valueOf(bookingDetailRes.getStarttime()));
        etTimenum.setText(String.valueOf(bookingDetailRes.getTimenumber()));
        etPrice.setText(String.valueOf(bookingDetailRes.getPrice()));
        etCarname.setText(bookingDetailRes.getCarname());
        etLisenceplates.setText(bookingDetailRes.getLisenceplates());

        //qrcode
//        ImageView imageView = findViewById(R.id.a_details_booking_imv_qrcode);
//        byte[] qrcontent = bookingDetailRes.getQrcontent();
//        Bitmap bmp = BitmapFactory.decodeByteArray(qrcontent, 0, qrcontent.length);
//        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 550, 550, false));

    }
}