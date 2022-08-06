package fpt.aptech.parkinggo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.DirectPaymentTask;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class DirectPaymentActivity extends AppCompatActivity {
    private TextView tvBalance;
    private EditText etAmount;
    private TextView tv20k;
    private TextView tv50k;
    private TextView tv100k;
    private ImageView imvQrcode;
    private Button btnQrcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_payment);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Direct Payment");
        //find element
        tvBalance = findViewById(R.id.a_dpayment_tv_balance);
        etAmount = findViewById(R.id.a_dpayment_et_amount);
        tv20k = findViewById(R.id.a_dpayment_tv_20k);
        tv50k = findViewById(R.id.a_dpayment_tv_50k);
        tv100k = findViewById(R.id.a_dpayment_tv_100k);
        imvQrcode = findViewById(R.id.a_dpayment_imv_qrcode);
        btnQrcode = findViewById(R.id.a_dpayment_btn_qrcode);
        //get balance
        NumberFormat formatter = new DecimalFormat("#,###");
        double dBalance = ((LoginRes) Session.getSession()).getBalance();
        String sBalance = formatter.format(dBalance);
        tvBalance.setText(sBalance + " VND");
        //event select amount
        tv20k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAmount.setText("20000");
                //tv20k.setTextColor(Color.parseColor("#24b2e5"));
                tv20k.setBackground(getDrawable(R.drawable.border_ms_blue));
                tv50k.setBackground(getDrawable(R.drawable.border_ms));
                tv100k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
        tv50k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAmount.setText("50000");
                //tv50k.setTextColor(Color.parseColor("#24b2e5"));
                tv50k.setBackground(getDrawable(R.drawable.border_ms_blue));
                tv20k.setBackground(getDrawable(R.drawable.border_ms));
                tv100k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
        tv100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv100k.setBackground(getDrawable(R.drawable.border_ms_blue));
                //tv100k.setTextColor(Color.parseColor("#24b2e5"));
                etAmount.setText("100000");
                tv50k.setBackground(getDrawable(R.drawable.border_ms));
                tv20k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
        //btn qrcode
        btnQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit(){
        try {
            double amount = Double.valueOf(etAmount.getText().toString());
            if (amount<=10000 || amount >=5000000){
                findViewById(R.id.a_dpayment_tv_error).setVisibility(View.VISIBLE);
                return;
            }else{
                DirectPaymentTask task = new DirectPaymentTask(this);
                task.execute();
            }
        }catch (Exception e){
            findViewById(R.id.a_dpayment_tv_error).setVisibility(View.VISIBLE);
            return;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(myIntent);
        return true;
    }
}