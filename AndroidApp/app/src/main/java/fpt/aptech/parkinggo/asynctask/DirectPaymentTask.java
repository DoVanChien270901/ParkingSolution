package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.configuration.RestTemplateConfiguration;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class DirectPaymentTask extends AsyncTask<Void, Void, ResponseEntity<?>> {
    private Activity activity;
    private EditText etAmount;

    public DirectPaymentTask (Activity activity){
        this.activity = activity;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        etAmount = activity.findViewById(R.id.a_dpayment_et_amount);
        String amount = etAmount.getText().toString();
        String URL = activity.getString(R.string.URL_BASE)+"qr-code/generated/payment?amount="+amount;
        String token = ((LoginRes) Session.getSession()).getToken();
        HttpEntity request  = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(URL, HttpMethod.POST,request, byte[].class);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseEntity<?> responseEntity) {
        ImageView imageView = activity.findViewById(R.id.a_dpayment_imv_qrcode);
        byte[] qrcontent = (byte[]) responseEntity.getBody();
        Bitmap bmp = BitmapFactory.decodeByteArray(qrcontent, 0, qrcontent.length);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 550, 550, false));
    }
}
