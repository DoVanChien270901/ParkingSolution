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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.DecimalFormat;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.EditProfileTask;
import fpt.aptech.parkinggo.asynctask.LoadProfileTask;
import fpt.aptech.parkinggo.callback.CallBack;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.statics.Session;

public class EditProfileActivity extends AppCompatActivity implements CallBack {
    private TextInputLayout etFullName,etEmail,tvDob,etPhone,etICard;
    //private ImageView qrcode;
    private Button btnSave;
    private DateTimeFormatter formatter;
    private String sDob;
    private LocalDate clDob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Edit Profile");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //load view
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        etFullName = findViewById(R.id.a_editprofile_et_fullname);
        etEmail = findViewById(R.id.a_editprofile_et_email);
        tvDob = findViewById(R.id.a_editprofile_tv_dob);
        etPhone = findViewById(R.id.a_editprofile_et_phone);
        etICard = findViewById(R.id.a_editprofile_et_icard);
        //qrcode = findViewById(R.id.a_editprofile_imv_qrcode);
        btnSave = findViewById(R.id.a_editprofile_btn_save);

        LoadProfileTask loadProfileTask = new LoadProfileTask(this, this::callback);
        loadProfileTask.execute();
        TextInputLayout tilDob = findViewById(R.id.a_editprofile_tv_dob);
        tilDob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDob.getEditText().setText(dayOfMonth + "-" + (new DecimalFormat("00").format(month+1)) + "-" + year);
//                        if (month < 10) {
//                            tvDob.getEditText().setText(dayOfMonth + "-" + (new DecimalFormat("00").format(month+1)) + "-" + year);
//                        } else {
//                            tvDob.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
//                        }
                    }
                }, clDob.getYear(), clDob.getMonthValue() - 1, clDob.getDayOfMonth());
                datePickerDialog.show();
            }
        });
        // click save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFullname() && validateEmail() && validatePhone() && validateIcard() &&validateDob()){
                    submit();
                }else{
                    return;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.actionbar_edit_profile_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getTitle() != null) {
//            EditProfileTask editProfileTask = new EditProfileTask(EditProfileActivity.this, this::callback);
//            editProfileTask.execute();
//            return true;
//        } else {
//            Intent myIntent = new Intent(EditProfileActivity.this, MapsActivity.class);
//            startActivity(myIntent);
//            return true;
//        }
//    }

    @Override
    public void callback(ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            ProfileRes profileRes = (ProfileRes) response.getBody();
            etFullName.getEditText().setText(profileRes.getFullname());
            //convert yyyy/mm/dd to dd/mm/yyyy
            sDob = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(profileRes.getDob());
            tvDob.getEditText().setText(sDob);
            clDob = LocalDate.parse(sDob, formatter);
            //tvDob.getEditText().setText(profileRes.getDob().toString());
            etEmail.getEditText().setText(profileRes.getEmail());
            etICard.getEditText().setText(profileRes.getIdentitycard().toString());
            etPhone.getEditText().setText(profileRes.getPhone().toString());
//            Bitmap bmp = BitmapFactory.decodeByteArray(profileRes.getQrcontent(), 0, profileRes.getQrcontent().length);
//            qrcode.setImageBitmap(Bitmap.createScaledBitmap(bmp, 650, 650, false));
            //update session
            ((LoginRes)Session.getSession()).setUsername(profileRes.getFullname());
            ((LoginRes)Session.getSession()).setQrcode(profileRes.getQrcontent());
            ((LoginRes)Session.getSession()).setBalance(profileRes.getBalance());
            ((LoginRes)Session.getSession()).setEmail(profileRes.getEmail());
        }

    }
    public void submit(){
        EditProfileTask editProfileTask = new EditProfileTask(EditProfileActivity.this, this::callback);
        editProfileTask.execute();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(this, MapsActivity.class);
        startActivity(myIntent);
        return true;
    }
    private boolean validateFullname() {
        if (etFullName.getEditText().getText().toString().length() < 3) {
            etFullName.setError("Full name must be more than 3 characters");
            return false;
        }
        else if(!etFullName.getEditText().getText().toString().matches("^[a-zA-Z_ ]*$")){
            etFullName.setError("Full name contains invalid characters");
            return false;
        }else {
            etFullName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        if (!etEmail.getEditText().getText().toString().matches("[a-z0-9]+@gmail.com")) {
            etEmail.setError("Please enter a valid your email");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        if (etPhone.getEditText().getText().toString().length() < 6 || etPhone.getEditText().getText().toString().length() > 13) {
            etPhone.setError("Please enter a valid your phone");
            return false;
        } else {
            etPhone.setError(null);
            return true;
        }
    }

    private boolean validateIcard() {
        if (etICard.getEditText().getText().toString().length() < 6||etICard.getEditText().getText().toString().length() > 15) {
            etICard.setError("Please enter a valid your identity card");
            return false;
        } else {
            etICard.setError(null);
            return true;
        }
    }
    private boolean validateDob() {
        if (tvDob.getEditText().getText().toString().length() < 6||tvDob.getEditText().getText().toString().length() > 15) {
            tvDob.setError("Please enter day of birth");
            return false;
        } else {
            tvDob.setError(null);
            return true;
        }
    }
}