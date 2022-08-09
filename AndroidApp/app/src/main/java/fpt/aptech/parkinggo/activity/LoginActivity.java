package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.LoginTask;
import fpt.aptech.parkinggo.domain.modelbuilder.LoginReqBuilder;
import fpt.aptech.parkinggo.domain.request.LoginReq;

public class LoginActivity extends AppCompatActivity {
private String a;
private TextInputLayout etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //find all element
        etUsername = findViewById(R.id.a_login_et_username);
        etPassword = findViewById(R.id.a_login_et_password);
        Button btnButton = findViewById(R.id.a_login_btn_signin);
        TextView tvRegister =findViewById(R.id.a_tv_register);

        //register now
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //validate
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
        //submit
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword()){
                    new LoginTask(LoginActivity.this).execute();
                }else{
                    return;
                }
            }
        });
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
}