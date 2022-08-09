package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.RegisterTask;
import fpt.aptech.parkinggo.domain.request.RegisterReq;

public class Register2Activity extends AppCompatActivity {
    private TextInputLayout etFullname, etEmail, etPhone, etICard, etGender, tvDob;
    private Button btnRegister;
    private AutoCompleteTextView actGender;
    private DateTimeFormatter formatter;
    private String sDob;
    private LocalDate clDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Register");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //find element
        sDob = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        clDob = LocalDate.parse(sDob, formatter);
        etFullname = findViewById(R.id.a_register2_et_fullname);
        etEmail = findViewById(R.id.a_register2_et_email);
        etPhone = findViewById(R.id.a_register2_et_phone);
        etICard = findViewById(R.id.a_register2_et_identity_card);
        tvDob = findViewById(R.id.a_register2_tv_dob);
        etGender = findViewById(R.id.a_register2_til_gender);
        btnRegister = findViewById(R.id.a_register2_btn_register);
        actGender = findViewById(R.id.a_register2_et_gender);
        //get value gender
        String []option = {"Male", "Female"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, option);
        actGender.setText("Gender", false);
        actGender.setAdapter(arrayAdapter);
    //vatidate
        //fullname
        etFullname.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFullname();
            }
        });
        //phone
        etPhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePhone();
            }
        });
        //email
        etEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
        });
        //icard
        etICard.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateIcard();
            }
        });
        tvDob.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sDob = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
                        tvDob.getEditText().setText(sDob);
                        clDob = LocalDate.parse(sDob, formatter);
//                        if (month < 10||dayOfMonth<10) {
////                            tvDob.getEditText().setText((new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year);
//                            sDob = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
//                            tvDob.getEditText().setText(sDob);
//                            clDob = LocalDate.parse(sDob, formatter);
//                        } else {
//                            //tvDob.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
//                            sDob = (new DecimalFormat("00").format(dayOfMonth)) + "-" + (new DecimalFormat("00").format(month + 1)) + "-" + year;
//                            tvDob.getEditText().setText(sDob);
//                            clDob = LocalDate.parse(sDob, formatter);
//                        }
                    }
                }, clDob.getYear(), clDob.getMonthValue() -1, clDob.getDayOfMonth());
                datePickerDialog.show();
            }
        });
        //submit
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return true;
    }

        public void submit(){
        if (validateFullname() && validateEmail()&& validatePhone() && validateIcard()&& validateGender()){
            RegisterReq registerReq = (RegisterReq) getIntent().getSerializableExtra("registerReq");
            registerReq.setFullname(etFullname.getEditText().getText().toString());
            registerReq.setEmail(etEmail.getEditText().getText().toString());
            registerReq.setPhone(Integer.valueOf(etPhone.getEditText().getText().toString()));
            registerReq.setIdentitycard(Integer.valueOf(etICard.getEditText().getText().toString()));
            registerReq.setDob(tvDob.getEditText().getText().toString());
            if (actGender.getText().equals("Female")){
                registerReq.setGender(false);
            }else if(actGender.getText().equals("Male")){
                registerReq.setGender(true);
            }
            ((RegisterTask) new RegisterTask(this)).execute(registerReq);
        }else{
            return;
        }

    }
    private boolean validateFullname() {
        if (etFullname.getEditText().getText().toString().length() < 3) {
            etFullname.setError("Full name must be more than 3 characters");
            return false;
        }
        else if(!etFullname.getEditText().getText().toString().matches("^[a-zA-Z_ ]*$")){
            etFullname.setError("Full name contains invalid characters");
            return false;
        }else {
            etFullname.setError(null);
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
    private boolean validateGender() {
        if (actGender.getText().toString().equals("Gender")) {
            etGender.setError("Please chose gender");
            return false;
        } else {
            etICard.setError(null);
            return true;
        }
    }
}