package fpt.aptech.parkinggo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import fpt.aptech.parkinggo.asynctask.LoadStatusParkingTask;
import fpt.aptech.parkinggo.domain.response.EBookingRes;
import fpt.aptech.parkinggo.domain.response.LoadStatusParking;
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
    private Button btnselectLocation;
    private RadioButton rbMomo;
    private RadioButton rbZalopay;
    private RadioButton rbWallet;
    private String message;

    private TableLayout tb;

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
        message = intent.getStringExtra("parkingname");
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

        btnselectLocation = findViewById(R.id.a_booking_btn_choselocation);
        btnselectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadDialog();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    if (bookingRes.getTransactionReq().getPaymentReq().getChannel().equals("Zalopay")) {
                        createZalopay(bookingRes);
                    } else if (bookingRes.getTransactionReq().getPaymentReq().getChannel().equals("Momo")) {
                        createMomopay(bookingRes);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createZalopay(EBookingRes bookingRes) {
        ZaloPaySDK.getInstance().payOrder(BookingActivity.this, bookingRes.getSignature(), "parkinggo://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                onPaymentSucces();
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
        BookingTask bookingTask = new BookingTask(BookingActivity.this, bookingRes);
        bookingTask.execute();
        new AlertDialog.Builder(BookingActivity.this)
                .setTitle("Payment Success")
                .setMessage("Thank for your order")
                .setPositiveButton("Go HomePage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BookingActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                }).show();
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
    public void loadDialog() throws ExecutionException, InterruptedException {
        LoadStatusParkingTask loadStatusParkingTask = new LoadStatusParkingTask(this, message);
        LoadStatusParking loadStatusParking = null;
        try {
            ResponseEntity<?> response = loadStatusParkingTask.execute().get();
            loadStatusParking = (LoadStatusParking) response.getBody();
        }catch (Exception e){}
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View alertView = inflater.inflate(R.layout.table_select_location_dialog, null);
        TableLayout tableLayout = alertView.findViewById(R.id.dialog_table);
        TableRow row = new TableRow(this);
        String selected = "A";
        int call = 0;
        for(int i = 0 ; i < loadStatusParking.getLocationcode().length ; i++){
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
            row.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView textView = new TextView(this);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(loadStatusParking.getLocationcode()[i]);
            for (int j = 0; j < loadStatusParking.getCodebooked().length; j++){
                if (loadStatusParking.getLocationcode()[i].equals(loadStatusParking.getCodebooked()[j])){
                    call = 1;
                }
            }
            if (call == 0){
                textView.setTextColor(Color.parseColor("#08FF00"));
                textView.setBackground(getDrawable(R.drawable.border_item_table_row_green));
            }else if(call==1){
                textView.setTextColor(Color.parseColor("#FF0000"));
                textView.setBackground(getDrawable(R.drawable.border_item_table_row_red));
                call = 0;
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTextView(textView, alertView);
                }
            });
            textView.setTextSize(12);
            textView.setWidth(70);
            textView.setHeight(70);
//            textView.setPadding(15, 15 ,15 ,15);
            row.addView(textView);
            if ((i+1) % loadStatusParking.getColumnofrow() == 0){
                tableLayout.addView(row);
                row = new TableRow(this);
            }
        }
        builder.setView(alertView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btnOK = alertView.findViewById(R.id.dialog_btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvLocationCode = findViewById(R.id.a_booking_et_locationcode);
                tvLocationCode.setText(textSelected);
                alertDialog.dismiss();
            }
        });

    }
    TextView selected = null;
    ColorStateList color;
    Drawable background;
    String textSelected;
    public void onClickTextView(TextView tv, View v){
        if (tv.getTextColors().getDefaultColor() == ((ColorStateList)ColorStateList.valueOf(Color.parseColor("#FF0000"))).getDefaultColor()){
            return;
        }
        if (selected == null){
            selected = tv;
            color = tv.getTextColors();
            background = tv.getBackground();
            tv.setTextColor(Color.parseColor("#2196F3"));
            tv.setBackground(getDrawable(R.drawable.border_item_table_row_blue));
            textSelected = tv.getText().toString();
            return;
        }
        if (selected !=null){
            selected.setTextColor(color);
            selected.setBackground(background);
            selected = tv;
            tv.setTextColor(Color.parseColor("#2196F3"));
            tv.setBackground(getDrawable(R.drawable.border_item_table_row_blue));
            textSelected = tv.getText().toString();
            return;
        }
    }
}