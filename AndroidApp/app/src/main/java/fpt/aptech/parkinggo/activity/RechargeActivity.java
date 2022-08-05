package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.CreateRechargeTask;
import fpt.aptech.parkinggo.asynctask.RechargeTask;
import fpt.aptech.parkinggo.domain.request.TransactionReq;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class RechargeActivity extends AppCompatActivity {

    //MOMo
    private Integer fee = 0;
    private String merchantName = "vinhvizg";
    private String merchantCode = "MOMOS40J20220512";
    private String description = "Thanh toán Ebooking";

    TransactionReq transactionReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        Button btnRecharge = findViewById(R.id.a_recharge_btn_recharge);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // Momopay init
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRechargeTask createRechargeTask = new CreateRechargeTask(RechargeActivity.this);
                try {
                    ResponseEntity<?> response = createRechargeTask.execute().get();
                    EPaymentRes ePaymentRes = (EPaymentRes) response.getBody();

                    if (ePaymentRes.getTransactionReq().getPaymentReq().getChannel().equals("Zalopay")) {
                        createZalopay(ePaymentRes);
                    } else if (ePaymentRes.getTransactionReq().getPaymentReq().getChannel().equals("Momo")) {
                        createMomopay(ePaymentRes);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createZalopay(EPaymentRes bookingRes) {
        ZaloPaySDK.getInstance().payOrder(RechargeActivity.this, bookingRes.getSignature(), "parkinggo://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                onPaymentSucces();
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                new AlertDialog.Builder(RechargeActivity.this)
                        .setTitle("User Cancel Payment")
                        .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }

            @Override
            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                new AlertDialog.Builder(RechargeActivity.this)
                        .setTitle("Payment Fail")
                        .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }
        });
    }

    //Get token through MoMo app
    private void createMomopay(EPaymentRes bookingRes) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Integer amount = Math.toIntExact(bookingRes.getTransactionReq().getAmount());

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", bookingRes.getTransNo()); //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);

    }
    protected void onPaymentSucces() {
        RechargeTask rechargeTask = new RechargeTask(RechargeActivity.this, transactionReq);
        rechargeTask.execute();
        new AlertDialog.Builder(RechargeActivity.this)
                .setTitle("Payment Success")
                .setMessage("Thank for your order")
                .setPositiveButton("Go HomePage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RechargeActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }
}