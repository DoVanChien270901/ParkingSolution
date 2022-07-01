package fpt.aptech.parkinggo.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.domain.response.ProfileRes;
import fpt.aptech.parkinggo.statics.Session;

public class LoadProfileTask extends AsyncTask<Void, Void, ResponseEntity<?>> {
    private View view;
    private EditText fullName;
    private EditText dob;
    private EditText email;
    private EditText iCard;
    private EditText phone;
    private EditText balance;
    private ImageView qrcode;
    public LoadProfileTask(View view) {
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        fullName = view.findViewById(R.id.f_profile_et_fullname);
        dob = view.findViewById(R.id.f_profile_et_dob);
        email = view.findViewById(R.id.f_profile_et_email);
        iCard = view.findViewById(R.id.f_profile_et_icard);
        phone = view.findViewById(R.id.f_profile_et_phone);
        balance = view.findViewById(R.id.f_profile_et_balance);
        qrcode = view.findViewById(R.id.f_profile_imv_qrcode);

    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        LoginRes account = (LoginRes) Session.getSession();
        HttpEntity request = RestTemplateConfiguration.setRequest(account.getToken());
        String uri = view.getResources().getString(R.string.URL_BASE)+"user";
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(uri, HttpMethod.GET, request, ProfileRes.class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> response) {
        if (response.getStatusCode() == HttpStatus.OK){
            ProfileRes profileRes = (ProfileRes) response.getBody();
            fullName.setText(profileRes.getFullname());
            //convert yyyy/mm/dd to dd/mm/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            dob.setText(formatter2.format(LocalDate.parse(profileRes.getDob(), formatter)));
            email.setText(profileRes.getEmail());
            iCard.setText(profileRes.getIdentitycard().toString());
            phone.setText(profileRes.getPhone().toString());
            balance.setText(profileRes.getBalance().toString());
            Bitmap bmp = BitmapFactory.decodeByteArray(profileRes.getQrContent(), 0, profileRes.getQrContent().length);
            qrcode.setImageBitmap(Bitmap.createScaledBitmap(bmp, 650, 650, false));
        }else{
            //no do thing
        }
    }
}
