package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.callback.CallBack;
import fpt.aptech.parkinggo.callback.CustomProgressDialog;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.modelbuilder.EditProfileReqBuilder;
import fpt.aptech.parkinggo.domain.modelbuilder.LoginReqBuilder;
import fpt.aptech.parkinggo.domain.request.EditProfileReq;
import fpt.aptech.parkinggo.domain.request.LoginReq;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.statics.Session;

public class EditProfileTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private CallBack callBack;
    private Activity activity;
    private CustomProgressDialog dialog;

    public EditProfileTask(Activity activity, CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        dialog = new CustomProgressDialog(activity);
        dialog.show();
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        EditText etFullname = activity.findViewById(R.id.a_editprofile_et_fullname);
        EditText etICard = activity.findViewById(R.id.a_editprofile_et_icard);
        EditText etDob = activity.findViewById(R.id.a_editprofile_et_dob);
        EditText etEmail = activity.findViewById(R.id.a_editprofile_et_email);
        EditText etPhone = activity.findViewById(R.id.a_editprofile_et_phone);
        //convert local date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(etDob.getText().toString(), formatter);
        EditProfileReq editProfileReq = new EditProfileReqBuilder()
                .setFullname(etFullname.getText().toString())
                .setIdentitycard(Integer.valueOf(etICard.getText().toString()))
                .setDob(etDob.getText().toString())
                .setEmail(etEmail.getText().toString())
                .setPhone(Integer.valueOf(etPhone.getText().toString()))
                .createEditProfileReq();

        HttpEntity request = RestTemplateConfiguration.setRequest(((LoginRes)Session.getSession()).getToken(),editProfileReq);
        String uri = activity.getString(R.string.URL_BASE);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri+"user", HttpMethod.PUT, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK){
            LoginRes loginRes = (LoginRes) Session.getSession();
            loginRes.setFullname(etFullname.getText().toString());
            loginRes.setEmail(etEmail.getText().toString());
            Session.setSession(loginRes);
        }
        return response;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        //load animation
        dialog.show();
    }
    @Override
    protected void onPostExecute(ResponseEntity<?> responseEntity) {
        LinearLayout myLayout = (LinearLayout) activity.findViewById(R.id.a_editprofile_main_layout);
        myLayout.requestFocus();
        dialog.dismiss();
    }

}
