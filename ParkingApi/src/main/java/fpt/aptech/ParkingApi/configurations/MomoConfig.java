package fpt.aptech.ParkingApi.configurations;

public class MomoConfig {

    public static String PARTNER_CODE = "MOMOS40J20220512";
    public static String ACCESS_KEY = "1Ei6fMBAQcqEZEKP";
    public static String SECRET_KEY = "jqgF75Ukf6NpjHw0OIQiqQZUlWanVBA7";
    public static String PAY_QUERY_STATUS_URL = "https://test-payment.momo.vn/pay/query-status";
    public static String PAY_CONFIRM_URL = "https://test-payment.momo.vn/pay/confirm";
    public static String RETURN_URL = "http://localhost:8080/api/momo/test";
    public static String NOTIFY_URL = "http://localhost:3000/success/payment";
    public static String IPN_URL = "https://5764-125-235-239-194.ap.ngrok.io";
    public static String CREATE_ORDER_URL_QR = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
    public static String REDIRECT_URL = "http://localhost:3000/success/payment";//url momo-api trả về -> tự động redirect tới url này
    public static String CREATE_ORDER_URL = "https://test-payment.momo.vn/v2/gateway/api/pos";
}
