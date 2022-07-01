/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.implementations;

import fpt.aptech.ParkingApi.interfaces.ITransaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import com.google.gson.*;
import fpt.aptech.ParkingApi.configurations.MomoConfig;
import fpt.aptech.ParkingApi.configurations.ZaloPayConfig;
import fpt.aptech.ParkingApi.dto.enumm.PaymentChannel;
import fpt.aptech.ParkingApi.dto.request.OrderReq;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import fpt.aptech.ParkingApi.utils.HMACUtil;
import fpt.aptech.ParkingApi.utils.MomoEncoderUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

@Service
public class TransactionService implements ITransaction {

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @Override
    public TransactionRes createOrder(OrderReq orderRequest) {
        switch (orderRequest.getChannel()) {
            case Momo:
                return createMomo(orderRequest);
            case Zalopay:
                return createZalopay(orderRequest);
            case ATM:
                return createZalopay(orderRequest);
            case Direct:
                return createDirect(orderRequest);
            default:
                //đơn hàng không hợp lệ
                break;
        }
        return null;
    }

    public TransactionRes createMomo(OrderReq orderRequest) {
        JSONObject json = new JSONObject();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        String returnUrl = MomoConfig.REDIRECT_URL;
        String notifyUrl = MomoConfig.NOTIFY_URL;

        String order_id = orderRequest.getTransNo();

        json.put("partnerCode", partnerCode);
        json.put("accessKey", accessKey);
        json.put("requestId", order_id);
        json.put("amount", orderRequest.getAmount().toString());
        json.put("orderId", order_id);
        json.put("orderInfo", "Nap tien " + order_id);
        json.put("returnUrl", returnUrl);
        json.put("notifyUrl", notifyUrl);
        json.put("requestType", "captureMoMoWallet");
//        json.put("requestType", "payWithATM");

        String data = "partnerCode=" + partnerCode
            + "&accessKey=" + accessKey
            + "&requestId=" + json.get("requestId")
            + "&amount=" + orderRequest.getAmount().toString()
            + "&orderId=" + json.get("orderId")
            + "&orderInfo=" + json.get("orderInfo")
            + "&returnUrl=" + returnUrl
            + "&notifyUrl=" + notifyUrl
            + "&extraData=";

        try {
            String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
            json.put("signature", hashData);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL_QR);
            StringEntity stringEntity = new StringEntity(json.toString());
            post.setHeader("content-type", "application/json");
            post.setEntity(stringEntity);

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            TransactionRes transactionRes = new TransactionRes();
            if (result.get("errorCode").toString().equalsIgnoreCase("0")) {
                transactionRes.setPayUrl(result.getString("payUrl"));
                transactionRes.setReturnCode(result.getInt("errorCode"));
                transactionRes.setReturnMessage(result.getString("localMessage"));
                transactionRes.setSignature(result.getString("signature"));
                transactionRes.setTransNo(result.getString("orderId"));

//                kq.put("requestType", result.get("requestType"));
//                kq.put("requestId", result.get("requestId"));
//                kq.put("message", result.get("message"));
                //deeplink
//            kq.addProperty("deeplink", result.get("deeplink"));
            } else {
                transactionRes.setReturnCode(result.getInt("errorCode"));
                transactionRes.setReturnMessage(result.getString("localMessage"));
                transactionRes.setSignature(result.getString("signature"));
                transactionRes.setTransNo(orderRequest.getTransNo());
//                transactionRes.setSignature(hashData);
//                transactionRes.setTransNo(order_id);

//                kq.put("requestId", result.get("requestId"));
//                kq.put("requestType", result.get("requestType"));
//                kq.put("message", result.get("message"));
//            kq.addProperty("deeplink", result.get("deeplink"));
            }
            return transactionRes;

        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public TransactionRes createZalopay(OrderReq orderRequest) {
        try {
            String transNo = orderRequest.getTransNo();
            Map<String, Object> zalopay_Params = new HashMap<>();
            zalopay_Params.put("appid", ZaloPayConfig.APP_ID);
            zalopay_Params.put("apptransid", transNo);
            zalopay_Params.put("apptime", System.currentTimeMillis());
            zalopay_Params.put("appuser", transNo);
            zalopay_Params.put("amount", orderRequest.getAmount());
            zalopay_Params.put("description", "Thanh toan don hang #" + transNo);
            zalopay_Params.put("bankcode", "");

            //viết lại item
            String item = "[{\"itemid\":\"knb\",\"itemname\":\"kim nguyen bao\",\"itemprice\":198400,\"itemquantity\":1}]";
            zalopay_Params.put("item", item);

            // embeddata
            // Trong trường hợp Merchant muốn trang cổng thanh toán chỉ hiện thị danh sách
            // các ngân hàng ATM,
            // thì Merchant để bankcode="" và thêm bankgroup = ATM vào embeddata như ví dụ
            // bên dưới
            // embeddata={"bankgroup": "ATM"}
            // bankcode=""
            Map<String, String> embeddata = new HashMap<>();

            //request hien thi danh sach ngan hang
            if (orderRequest.getTransType().equals("ATM")) {
                embeddata.put("bankgroup", "ATM");
            }

            embeddata.put("merchantinfo", "ParkingBooking");
            embeddata.put("promotioninfo", "");
            //trang trả về thông tin thanh toán của web
            embeddata.put("redirecturl", ZaloPayConfig.REDIRECT_URL);

            Map<String, String> columninfo = new HashMap<>();
            //StoreName
            columninfo.put("store_name", "Parking");

            //map - > JsonObject -> String
            embeddata.put("columninfo", new Gson().toJson(columninfo));
            zalopay_Params.put("embeddata", new Gson().toJson(embeddata));

            String data = zalopay_Params.get("appid") + "|" + zalopay_Params.get("apptransid") + "|"
                + zalopay_Params.get("appuser") + "|" + zalopay_Params.get("amount") + "|"
                + zalopay_Params.get("apptime") + "|" + zalopay_Params.get("embeddata") + "|"
                + zalopay_Params.get("item");
            zalopay_Params.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZaloPayConfig.KEY1, data));
//		zalopay_Params.put("phone", order.getPhone());
//		zalopay_Params.put("email", order.getEmail());
//		zalopay_Params.put("address", order.getAddress());
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(ZaloPayConfig.CREATE_ORDER_URL);

            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> e : zalopay_Params.entrySet()) {
                params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
            }
            post.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }
            JSONObject result = new JSONObject(resultJsonStr.toString());

            TransactionRes transactionRes = new TransactionRes();
            Integer returncode = (Integer) result.get("returncode");

            transactionRes.setPayUrl(result.getString("orderurl"));
//            transactionRes.setReturnCode(result.getInt("returncode"));
            transactionRes.setReturnCode(returncode);
            transactionRes.setReturnMessage(result.getString("returnmessage"));
            transactionRes.setSignature(result.getString("zptranstoken"));
            transactionRes.setTransNo(transNo);

//
//            Map<String, Object> kq = new HashMap<>();
//        kq.put("returnmessage", result.get("returnmessage"));
//        kq.put("orderurl", result.get("orderurl"));
//        kq.put("returncode", result.get("returncode"));
//        kq.put("zptranstoken", result.get("zptranstoken"));
//        kq.put("apptransid", apptransid);
            return transactionRes;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public TransactionRes createDirect(OrderReq orderRequest) {
        return null;
    }
}
