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
import fpt.aptech.ParkingApi.dto.request.CheckStatusPaymentReq;
import fpt.aptech.ParkingApi.dto.request.CreateOrderReq;
import fpt.aptech.ParkingApi.dto.request.TransactionReq;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.dto.response.EPaymentRes;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Transactioninformation;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.repositorys.TransactionRepo;
import fpt.aptech.ParkingApi.utils.HMACUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.MomoEncoderUtils;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Service
public class TransactionService implements ITransaction {

    @Autowired
    private ParkingRepo _parkingRepo;
    @Autowired
    private ProfileRepo _proProfileRepository;
    @Autowired
    private TransactionRepo _transactionRepository;
    @Autowired
    private ModelMapperUtil _mapper;

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @Override
    public EPaymentRes createOrder(CreateOrderReq orderRequest) {
        System.out.println(orderRequest.getTransno());
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

    //check status Transaction -> save on database
    @Override
    public EPaymentRes checkStatus(CheckStatusPaymentReq checkStatusReq) {
        switch (checkStatusReq.getChannel()) {
            case Momo:
                return checkStatusMomo(checkStatusReq);
            case Zalopay:
                return checkStatusZalopay(checkStatusReq);
            case ATM:
                return checkStatusZalopay(checkStatusReq);
            case Direct:
            //return createDirect(orderRequest);
            default:
                //đơn hàng không hợp lệ
                break;
        }
        return null;
    }

    @Override
    public PageTransactionRes findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Transactioninformation> pageTrans = _transactionRepository.findAll(pageRequest);
        List<TransactionRes> listTrans = _mapper.mapList(pageTrans.getContent(), TransactionRes.class);

        PageTransactionRes pageTransactionRes = new PageTransactionRes();
        pageTransactionRes.setListTransaction(listTrans);
        pageTransactionRes.setCurrentPage(pageTrans.getPageable().getPageNumber());
        pageTransactionRes.setSize(pageTrans.getSize());
        pageTransactionRes.setTotalPages(pageTrans.getTotalPages());
        return pageTransactionRes;
    }

    @Override
    public PageTransactionRes getByUserName(String username, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<TransactionRes> trans = _transactionRepository.getListTransByUsername(username);
        Page<TransactionRes> pageTrans = new PageImpl<>(trans, pageRequest, trans.size());
        List<TransactionRes> listTrans = _mapper.mapList(pageTrans.getContent(), TransactionRes.class);

        PageTransactionRes pageTransactionRes = new PageTransactionRes();
        pageTransactionRes.setCurrentPage(pageTrans.getPageable().getPageNumber());
        pageTransactionRes.setListTransaction(listTrans);
        pageTransactionRes.setSize(pageTrans.getSize());
        pageTransactionRes.setTotalPages(pageTrans.getTotalPages());
        return pageTransactionRes;
    }

    public EPaymentRes createMomo(CreateOrderReq orderRequest) {
        JSONObject json = new JSONObject();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        String returnUrl = MomoConfig.REDIRECT_URL;
        String notifyUrl = MomoConfig.NOTIFY_URL;

        String order_id = orderRequest.getTransno();

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
            post.setHeader("content-type", "application/json;charset=UTF-8\"");
            post.setEntity(stringEntity);

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            EPaymentRes transactionRes = new EPaymentRes();
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
                transactionRes.setTransNo(orderRequest.getTransno());
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

    public EPaymentRes createZalopay(CreateOrderReq orderRequest) {
        try {
            String transNo = orderRequest.getTransno();
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
            if (orderRequest.getChannel().equals(PaymentChannel.ATM)) {
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

            EPaymentRes transactionRes = new EPaymentRes();
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

    public EPaymentRes createDirect(CreateOrderReq orderRequest) {
        return null;
    }

    public EPaymentRes checkStatusMomo(CheckStatusPaymentReq checkStatusReq) {

        try {
            JSONObject json = new JSONObject();
            String partnerCode = MomoConfig.PARTNER_CODE;
            String accessKey = MomoConfig.ACCESS_KEY;
            String secretKey = MomoConfig.SECRET_KEY;
            json.put("partnerCode", partnerCode);
            json.put("accessKey", accessKey);
            json.put("requestId", checkStatusReq.getTransno());
            json.put("orderId", checkStatusReq.getTransno());
            json.put("requestType", "transactionStatus");

            String data = "partnerCode=" + partnerCode + "&accessKey=" + accessKey + "&requestId=" + json.get("requestId")
                + "&orderId=" + json.get("orderId") + "&requestType=" + json.get("requestType");
            String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
            json.put("signature", hashData);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL_QR);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
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

            EPaymentRes transactionRes = new EPaymentRes();
            transactionRes.setReturnCode(result.getInt("errorCode"));
            transactionRes.setTransNo(result.getString("orderId"));
            transactionRes.setReturnMessage(result.getString("localMessage"));
            transactionRes.setSignature(result.getString("signature"));

//            Map<String, Object> kq = new HashMap<>();
//            kq.put("requestId", result.get("requestId"));
//            kq.put("orderId", result.get("orderId"));
//            kq.put("extraData", result.get("extraData"));
//            kq.put("amount", Long.parseLong(result.get("amount").toString()));
//            kq.put("transId", result.get("transId"));
//            kq.put("payType", result.get("payType"));
//            kq.put("errorCode", result.get("errorCode"));
//            kq.put("message", result.get("message"));
//            kq.put("localMessage", result.get("localMessage"));
//            kq.put("requestType", result.get("requestType"));
//            kq.put("signature", result.get("signature"));
            return transactionRes;

        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public EPaymentRes checkStatusZalopay(CheckStatusPaymentReq checkStatusReq) {
        try {
            String appid = ZaloPayConfig.APP_ID;
            String key1 = ZaloPayConfig.KEY1;
            String data = appid + "|" + checkStatusReq.getTransno() + "|" + key1; // appid|apptransid|key1
            String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key1, data);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("appid", appid));
            params.add(new BasicNameValuePair("apptransid", checkStatusReq.getTransno()));
            params.add(new BasicNameValuePair("mac", mac));

            URIBuilder uri = new URIBuilder("https://sandbox.zalopay.com.vn/v001/tpe/getstatusbyapptransid");
            uri.addParameters(params);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(uri.build());

            CloseableHttpResponse res = client.execute(get);

            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "UTF-8"));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            EPaymentRes transactionRes = new EPaymentRes();
            transactionRes.setReturnCode(result.getInt("returncode"));
            transactionRes.setReturnMessage(result.getString("returnmessage"));
            transactionRes.setSignature(String.valueOf(result.getBoolean("isprocessing")));
            transactionRes.setTransNo(String.valueOf(result.getLong("zptransid")));

//            Map<String, Object> kq = new HashMap<>();
//            kq.put("returncode", result.get("returncode"));
//            kq.put("returnmessage", result.get("returnmessage"));
//            kq.put("isprocessing", result.get("isprocessing"));
//            kq.put("amount", result.get("amount"));
//            kq.put("discountamount", result.get("discountamount"));
//            kq.put("zptransid", result.get("zptransid"));
            return transactionRes;
        } catch (IOException | UnsupportedOperationException | URISyntaxException ex) {
            Logger.getLogger(TransactionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Transactioninformation create(TransactionReq transactionReq, int statuscode) {
        Transactioninformation transInfo = new Transactioninformation();
        transInfo.setAccountid(_proProfileRepository.getByUsername(transactionReq.getUsername()));
        transInfo.setAmount(Double.valueOf(transactionReq.getAmount()));
        transInfo.setChannel(transactionReq.getPaymentReq().getChannel().toString());
        transInfo.setDatetime(LocalDateTime.now());
        transInfo.setDescription(transactionReq.getUsername() + " - " + transactionReq.getPaymentReq().getTransno() + " - " + transactionReq.getStype());
        transInfo.setStatuscode(statuscode);
        transInfo.setStype(transactionReq.getStype());
        transInfo.setTransno(transactionReq.getPaymentReq().getTransno());
        if (statuscode == 0) {
            Double balance = _proProfileRepository.getBalanceByUsername(transactionReq.getUsername());
            Double amount = Double.valueOf(transactionReq.getAmount());
            if (transactionReq.getStype().equals("e-Recharge")) {
                _proProfileRepository.updateBalanceByUsername(balance + amount, transactionReq.getUsername());
            } else if (transactionReq.getStype().equals("e-Booking")) {
                transInfo.setParkingname(_parkingRepo.getByName(transactionReq.getParkingname()));
            }
        }
        return _transactionRepository.save(transInfo);
    }

    @Override
    public Transactioninformation getbyTransNo(String transno) {

        return _transactionRepository.getByTransNo(transno);
    }
}
