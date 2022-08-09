package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.CreateRechargeTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;

public class RechargeActivity extends AppCompatActivity {

    EPaymentRes rechargeRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        Button btnRecharge = findViewById(R.id.a_recharge_btn_recharge);
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRechargeTask rechargeTask = new CreateRechargeTask(RechargeActivity.this);

                try {
                    ResponseEntity<?> res = rechargeTask.execute().get();
                    rechargeRes = (EPaymentRes) res.getBody();
                    Intent Payintent = new Intent(RechargeActivity.this, PaymentActivity.class);
                    Payintent.putExtra("bookingRes", rechargeRes);
                    startActivity(Payintent);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}