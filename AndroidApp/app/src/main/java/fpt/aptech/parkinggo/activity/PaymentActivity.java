package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.RechargeTask;
import fpt.aptech.parkinggo.domain.response.EPaymentRes;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {

    //MOMo
    private Integer fee = 0;
    int environment = 0;//developer default
    private String merchantName = "vinhvizg";
    private String merchantCode = "MOMOS40J20220512";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán Ebooking";

    EPaymentRes paymentRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // Momopay init
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);


        paymentRes = (EPaymentRes) getIntent().getSerializableExtra("bookingRes");
        
        if (paymentRes.getTransactionReq().getPaymentReq().getChannel().equals("Zalopay")) {
            createZalopay(paymentRes);
        } else if (paymentRes.getTransactionReq().getPaymentReq().getChannel().equals("Momo")) {
            createMomopay(paymentRes);
        }
    }

    private void createZalopay(EPaymentRes paymentRes) {
        ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, paymentRes.getSignature(), "parkinggo://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                onPaymentSucces();
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                new AlertDialog.Builder(PaymentActivity.this)
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
                new AlertDialog.Builder(PaymentActivity.this)
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
    private void createMomopay(EPaymentRes paymentRes) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Integer amount = Math.toIntExact(paymentRes.getTransactionReq().getAmount());

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", paymentRes.getTransNo()); //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
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

    //Get token callback from MoMo app an submit to server side
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
//                    String token = data.getStringExtra("data"); //Token response
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String env = data.getStringExtra("env");
//                    if (env == null) {
//                        env = "app";
//                    }
                    onPaymentSucces();
//                    if (token != null && !token.equals("")) {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                    }
                }
//                else if (data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
//                    tvMessage.setText("message: " + message);
//                } else if (data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                } else {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                }
            }
//            else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//            }
        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }

    protected void onPaymentSucces() {
        if (paymentRes.getTransactionReq().getStype().equals("e-Booking")){
            BookingTask bookingTask = new BookingTask(PaymentActivity.this, paymentRes);
            bookingTask.execute();
        }else if (paymentRes.getTransactionReq().getStype().equals("e-Recharge")){
            RechargeTask rechargeTask = new RechargeTask(PaymentActivity.this, paymentRes);
            rechargeTask.execute();
        }

        new AlertDialog.Builder(PaymentActivity.this)
                .setTitle("Payment Success")
                .setMessage("Thank for your order")
                .setPositiveButton("Go HomePage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}