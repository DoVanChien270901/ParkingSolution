package fpt.aptech.parkinggo.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import org.springframework.http.ResponseEntity;

import fpt.aptech.parkinggo.domain.response.EPaymentRes;

public class PaymentTask extends AsyncTask<Void, Integer, ResponseEntity<?>> {
    private Activity activity;

    public PaymentTask() {
    }

    public PaymentTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ResponseEntity<?> doInBackground(Void... voids) {
        return null;
    }
}
