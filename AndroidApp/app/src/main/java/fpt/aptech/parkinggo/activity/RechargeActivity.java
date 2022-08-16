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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.CreateRechargeTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import fpt.aptech.parkinggo.domain.response.LoginRes;
import fpt.aptech.parkinggo.statics.Session;

public class RechargeActivity extends AppCompatActivity {
    private TextInputLayout etAmount;
    private TextView tv20k, tv50k,tv100k,tvbalance;
    EPaymentRes rechargeRes = null;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Title Bar
        actionBar.setTitle("Electronic Payment");
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        //find all element
        etAmount = findViewById(R.id.a_epayment_et_amount);
        tv20k = findViewById(R.id.a_epayment_tv_20k);
        tv50k = findViewById(R.id.a_epayment_tv_50k);
        tv100k = findViewById(R.id.a_epayment_tv_100k);
        tvbalance = findViewById(R.id.a_epayment_tv_balance);
        Button btnRecharge = findViewById(R.id.a_recharge_btn_recharge);
        //set set balance
        tvbalance.setText(formatInteger(((LoginRes) Session.getSession()).getBalance().toString()) + "đ");
        //submit
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = Double.valueOf(etAmount.getEditText().getText().toString().replace("đ", "").replaceAll("[,]", ""));;
                if (amount<=10000 || amount >=5000000){
                    etAmount.setError("valid amount between 10,000đ to 5,000,000đ");
                    return;
                }
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
        //format vnd
        etAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current) && s.toString().replace("đ", "").length()>=1){
                    etAmount.getEditText().removeTextChangedListener(this);
                    String a = etAmount.getEditText().getText().toString();
                    String cleanString = a.replace("đ", "").replaceAll("[,]", "");
                    String amountFormat = formatInteger(cleanString)+"đ";
                    current = amountFormat;
                    etAmount.getEditText().setText(amountFormat);
                    etAmount.getEditText().setSelection(amountFormat.length()-1);
                    etAmount.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //event 20k
        tv20k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAmount.getEditText().setText("20000");
                //tv20k.setTextColor(Color.parseColor("#24b2e5"));
                tv20k.setBackground(getDrawable(R.drawable.border_ms_blue));
                tv50k.setBackground(getDrawable(R.drawable.border_ms));
                tv100k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
        //even tv50k
        tv50k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAmount.getEditText().setText("50000");
                //tv50k.setTextColor(Color.parseColor("#24b2e5"));
                tv50k.setBackground(getDrawable(R.drawable.border_ms_blue));
                tv20k.setBackground(getDrawable(R.drawable.border_ms));
                tv100k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
        //even tv100k
        tv100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv100k.setBackground(getDrawable(R.drawable.border_ms_blue));
                //tv100k.setTextColor(Color.parseColor("#24b2e5"));
                etAmount.getEditText().setText("100000");
                tv50k.setBackground(getDrawable(R.drawable.border_ms));
                tv20k.setBackground(getDrawable(R.drawable.border_ms));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
        return true;
    }
    private String formatInteger(String str) {
        BigDecimal parsed = new BigDecimal(str);
        DecimalFormat formatter =
                new DecimalFormat( "#,###", new DecimalFormatSymbols(Locale.US));
        return formatter.format(parsed);
    }
}