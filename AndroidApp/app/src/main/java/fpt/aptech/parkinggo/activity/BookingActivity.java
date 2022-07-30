package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fpt.aptech.parkinggo.R;
import fpt.aptech.parkinggo.asynctask.BookingTask;
import fpt.aptech.parkinggo.asynctask.CreatePaymentTask;
import fpt.aptech.parkinggo.asynctask.PaymentTask;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BookingActivity extends AppCompatActivity {

    TextView parkingname;

    private EditText etDatetime;
    private EditText etCarname;
    private EditText etLisenceplates;
    private EditText etTimenumber;

    private Button btnBook;
    private RadioButton rbMomo;
    private RadioButton rbZalopay;
    private RadioButton rbWallet;


    //MOMo
    private Integer fee = 0;
    int environment = 0;//developer default
    private String merchantName = "vinhvizg";
    private String merchantCode = "MOMOS40J20220512";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Thanh toán Ebooking";

    EBookingRes bookingRes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //EditText
        etCarname = findViewById(R.id.a_booking_et_carname);
        etLisenceplates = findViewById(R.id.a_booking_et_lisenceplates);
        etTimenumber = findViewById(R.id.a_booking_et_timenum);

        //get Parking Name
        Intent intent = getIntent();
        String message = intent.getStringExtra("parkingname");
        parkingname = findViewById(R.id.a_booking_et_parkingname);
        parkingname.setText(message);

        //DateTime Picker
        etDatetime = findViewById(R.id.a_booking_et_date);
        etDatetime.setInputType(InputType.TYPE_NULL);
        etDatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(etDatetime);
            }
        });

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // Momopay init
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        //Booking
        btnBook = findViewById(R.id.a_booking_btn_book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePaymentTask CreatePaymentTask = new CreatePaymentTask(BookingActivity.this);
                try {
                    ResponseEntity<?> res = CreatePaymentTask.execute().get();
                    bookingRes = (EBookingRes) res.getBody();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                if (bookingRes.getTransactionReq().getPaymentReq().getChannel().equals("Zalopay")) {
                    createZalopay(bookingRes);
                } else if (bookingRes.getTransactionReq().getPaymentReq().getChannel().equals("Momo")) {
                    createMomopay(bookingRes);
                }
            }
        });
    }

    private void createZalopay(EBookingRes bookingRes) {
        ZaloPaySDK.getInstance().payOrder(BookingActivity.this, bookingRes.getSignature(), "parkinggo://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPaymentSucces();
                        new AlertDialog.Builder(BookingActivity.this)
                                .setTitle("Payment Success")
                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                });
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                new AlertDialog.Builder(BookingActivity.this)
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
                new AlertDialog.Builder(BookingActivity.this)
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
    private void createMomopay(EBookingRes bookingRes) {
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
        PaymentTask paymentTask = new PaymentTask(BookingActivity.this, bookingRes);
        BookingTask bookingTask = new BookingTask(BookingActivity.this, bookingRes);
        try {
            ResponseEntity<?> res = bookingTask.execute().get();
            Integer bookingId = (Integer) res.getBody();
//            ResponseEntity<?> res2 = paymentTask.execute().get();
//            Integer bookingId = (Integer) res2.getBody();
//            TransactionReq eBookingRes = (TransactionReq) res.getBody();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void showDateDialog(EditText etDatetime) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        BookingActivity.this.etDatetime.setText(dateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(BookingActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(BookingActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}