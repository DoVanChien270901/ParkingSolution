package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.activity.MapsActivity;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.request.RegisterReq;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.statics.Session;

public class RegisterTask extends AsyncTask<RegisterReq, Integer, ResponseEntity<?>> {
    private Activity activity;
    private CustomProgressDialog dialog;
    public RegisterTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        dialog = new CustomProgressDialog(activity);
        dialog.show();
    }
    @Override
    protected ResponseEntity<?> doInBackground(RegisterReq... registerReq) {
        HttpEntity request = RestTemplateConfiguration.setRequest(registerReq[0]);
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration.excuteRequest(uri+"register" , HttpMethod.POST, request, LoginRes.class);
        if (response.getStatusCode() == HttpStatus.OK){
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
            if (loginRes.getRole().toString().equals("user")){
                intent = new Intent(activity.getApplicationContext(), MapsActivity.class);
                dialog.dismiss();
                activity.startActivity(intent);
            }else{
                dialog.dismiss();
                Toast.makeText(activity.getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
            }
//          Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
        } else {
            dialog.dismiss();
            Toast.makeText(activity.getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
        }
    }
}
