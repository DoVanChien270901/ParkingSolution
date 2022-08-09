package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.domain.request.RegisterReq;

public class RegisterActivity extends AppCompatActivity {
private TextInputLayout etUsername, etPassword, etConfirmPass;
private TextView tvLoginHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Register");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        CustomProgressDialog dialog = new CustomProgressDialog(RegisterActivity.this);
        etUsername = findViewById(R.id.a_register_et_username);
        etPassword = findViewById(R.id.a_register_et_password);
        etConfirmPass = findViewById(R.id.a_register_et_confirm_password);
        etConfirmPass = findViewById(R.id.a_register_et_confirm_password);
        tvLoginHere = findViewById(R.id.a_register_tv_login_here);
        Button button = findViewById(R.id.a_register_btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    //validate onkey change
        //username
        etUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateUsername();
            }
        });
        //password
        etPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
            }
        });
        //confirm pass
        etConfirmPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmPassword();
            }
        });
    //click login here
        tvLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
    //back
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(myIntent);
        return true;
    }

    //submid
    public void submit(){
        if (validateUsername() && validatePassword() && validateConfirmPassword()){
            Intent intent = new Intent(this, Register2Activity.class);
            RegisterReq registerReq = new RegisterReq();
            registerReq.setUsername(etUsername.getEditText().getText().toString());
            registerReq.setPassword(etPassword.getEditText().getText().toString());
            intent.putExtra("registerReq", registerReq);
            startActivity(intent);
        }else{
            return;
        }
    }
    public void checkIntern(){
        Intent intentput = getIntent();
        RegisterReq registerReq = (RegisterReq) getIntent().getSerializableExtra("registerReq");
        if (registerReq!=null){
            etUsername.getEditText().setText(registerReq.getUsername());
            etPassword.getEditText().setText(registerReq.getPassword());
            etConfirmPass.getEditText().setText(registerReq.getPassword());
        }
    }
    private boolean validateUsername(){
        if (etUsername.getEditText().getText().toString().length() < 6){
            etUsername.setError("Username must be more than 6 characters");
            return false;
        }else{
            etUsername.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        if (etPassword.getEditText().getText().toString().length() < 6){
            etPassword.setError("Password must be more than 6 characters");
            return false;
        }else{
            etPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword(){
        String valPassword = etPassword.getEditText().getText().toString();
        String valConfirmPass = etConfirmPass.getEditText().getText().toString();
        if (!valConfirmPass.equals(valPassword)){
            etConfirmPass.setError("Confirm password is not math");
            return false;
        }else{
            etConfirmPass.setError(null);
            return true;
        }
    }
}