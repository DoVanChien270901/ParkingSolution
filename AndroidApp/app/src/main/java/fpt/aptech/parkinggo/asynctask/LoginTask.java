package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import android.content.*;
import android.widget.Toast;

import fpt.aptech.parkinggo.R;

import fpt.aptech.parkinggo.activity.HomeActivity;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.LoginReqBuilder;
import fpt.aptech.parkinggo.domain.request.LoginReq;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.statics.Session;

public class LoginTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;
    private EditText etUsername;
    private EditText etPassword;
    private CustomProgressDialog dialog;
    public LoginTask() {
    }

    public LoginTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
         etUsername = activity.findViewById(R.id.a_login_et_username);
         etPassword = activity.findViewById(R.id.a_login_et_password);
        if (!etUsername.getText().toString().matches("[a-z0-9_-]{6,12}$")) {
            etUsername.requestFocus();
            etUsername.setError("Username must be between 6 and 25 characters");
            this.cancel(true);
        } else if (!etPassword.getText().toString().matches("[a-z0-9_-]{6,12}$")) {
            etPassword.requestFocus();
            etPassword.setError("Password must be between 6 and 25 characters");
            this.cancel(true);
        }else{
            dialog = new CustomProgressDialog(activity);
            dialog.show();
        }
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        EditText etUsername = activity.findViewById(R.id.a_login_et_username);
        EditText etPassword = activity.findViewById(R.id.a_login_et_password);
        LoginReq loginReq = new LoginReqBuilder()
                .setUsername(etUsername.getText().toString())
                .setPassword(etPassword.getText().toString())
                .createLoginReq();
        HttpEntity request = RestTemplateConfiguration.setRequest(loginReq);
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri+"authenticate", HttpMethod.POST, request, LoginRes.class);
        if (response.getStatusCode() ==HttpStatus.OK){
            LoginRes loginRes = (LoginRes) response.getBody();
            HttpEntity request1 = RestTemplateConfiguration.setRequest(loginRes.getToken());
            ResponseEntity<?> response1 = RestTemplateConfiguration
                    .excuteRequest(uri+"user", HttpMethod.GET, request1, ProfileRes.class);
            loginRes.setQrcode(((ProfileRes)response1.getBody()).getQrcontent());
            Session.setSession(loginRes);
        }
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            LoginRes loginRes = (LoginRes) response.getBody();
            Session.setSession(loginRes);
            Intent intent;
            switch (loginRes.getRole()){
                case user: intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                case admin: intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                case handle: intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                default: intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
            }
            dialog.dismiss();
            activity.startActivity(intent);
//          Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
        } else {
            dialog.dismiss();
            Toast.makeText(activity.getApplicationContext(), "User name or password is valid", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        //load animation
//        dialog.show();
//    }
}