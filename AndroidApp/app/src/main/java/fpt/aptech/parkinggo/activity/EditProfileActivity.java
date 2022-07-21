package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.EditProfileTask;
import fpt.aptech.parkinggo.asynctask.LoadProfileTask;
import fpt.aptech.parkinggo.callback.CallBack;
import fpt.aptech.parkinggo.domain.response.ProfileRes;

public class EditProfileActivity extends AppCompatActivity implements CallBack {
    private EditText etFullName;
    private EditText etEmail;
    private EditText etGender;
    private EditText etDob;
    private EditText etPhone;
    private EditText etICard;
    private ImageView qrcode;
    private int dobyear;
    private int dobmonth;
    private int dobday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#034981"));
        // Title Bar
        actionBar.setTitle("Edit Profile");
        actionBar.setBackgroundDrawable(colorDrawable);
        //load view
        etFullName = findViewById(R.id.a_editprofile_et_fullname);
        etEmail = findViewById(R.id.a_editprofile_et_email);
        etGender = findViewById(R.id.a_editprofile_et_gender);
        etDob = findViewById(R.id.a_editprofile_et_dob);
        etPhone = findViewById(R.id.a_editprofile_et_phone);
        etICard = findViewById(R.id.a_editprofile_et_icard);
        qrcode = findViewById(R.id.a_editprofile_imv_qrcode);
        LoadProfileTask loadProfileTask = new LoadProfileTask(this, this::callback);
        loadProfileTask.execute();
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (month < 10) {
                            etDob.setText(year + "-" + (new DecimalFormat("00").format(month+1)) + "-" + dayOfMonth);
                        } else {
                            etDob.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }
                    }
                }, dobyear, dobmonth - 1, dobday);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() != null) {
            EditProfileTask editProfileTask = new EditProfileTask(EditProfileActivity.this, this::callback);
            editProfileTask.execute();
            return true;
        } else {
            Intent myIntent = new Intent(EditProfileActivity.this, HomeActivity.class);
            startActivity(myIntent);
            return true;
        }
    }

    @Override
    public void callback(ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            ProfileRes profileRes = (ProfileRes) response.getBody();
            etFullName.setText(profileRes.getFullname());
            //convert yyyy/mm/dd to dd/mm/yyyy
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/YYYY");
//            etDob.setText(formatter2.format(LocalDate.parse(profileRes.getDob(), formatter)));
            etDob.setText(profileRes.getDob().toString());
            etEmail.setText(profileRes.getEmail());
            etICard.setText(profileRes.getIdentitycard().toString());
            etPhone.setText(profileRes.getPhone().toString());
            Bitmap bmp = BitmapFactory.decodeByteArray(profileRes.getQrcontent(), 0, profileRes.getQrcontent().length);
            qrcode.setImageBitmap(Bitmap.createScaledBitmap(bmp, 650, 650, false));
            dobyear = profileRes.getDob().getYear();
            dobmonth = profileRes.getDob().getMonthValue();
            dobday = profileRes.getDob().getDayOfMonth();
        }

    }
}