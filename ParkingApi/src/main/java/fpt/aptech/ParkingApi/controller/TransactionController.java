/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.PaymentChannel;
import fpt.aptech.ParkingApi.dto.request.CheckStatusPaymentReq;
import fpt.aptech.ParkingApi.dto.request.CreateOrderReq;
import fpt.aptech.ParkingApi.dto.request.EBookingReq;
import fpt.aptech.ParkingApi.dto.request.ERechargeReq;
import fpt.aptech.ParkingApi.dto.request.TransactionReq;
import fpt.aptech.ParkingApi.dto.response.EPaymentRes;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import fpt.aptech.ParkingApi.entities.Transactioninformation;
import fpt.aptech.ParkingApi.interfaces.ITransaction;
import fpt.aptech.ParkingApi.repositorys.ParkingRepo;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PCvinhvizg
 */
@RestController
public class TransactionController {

    @Autowired
    private ParkingRepo _parkingRepo;
    @Autowired
    private JwtUtil _jwtTokenUtil;
    @Autowired
    private ITransaction _transactionServices;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
        return "Test";
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @RequestMapping(value = "/e-recharge", method = RequestMethod.POST)
    public ResponseEntity<?> eRecharge(@RequestBody ERechargeReq rechargeReq) {
        //something code
        try {
            CreateOrderReq orderRequest = new CreateOrderReq();
            switch (rechargeReq.getChannel()) {
                case "Momo":
                    orderRequest.setChannel(PaymentChannel.Momo);
                    break;
                case "Zalopay":
                    orderRequest.setChannel(PaymentChannel.Zalopay);
                    break;
                case "ATM":
                    orderRequest.setChannel(PaymentChannel.ATM);
            }
            orderRequest.setTransno(getCurrentTimeString("yyMMdd") + "_" + new Date().getTime());
            orderRequest.setAmount(rechargeReq.getAmount());
            orderRequest.setStype("e-Recharge");
            EPaymentRes transactionRes = _transactionServices.createOrder(orderRequest);
            TransactionReq transactionReq = new TransactionReq();
            transactionReq.setAmount(rechargeReq.getAmount());
            transactionReq.setStype(orderRequest.getStype());
            CheckStatusPaymentReq statusPaymentReq = new CheckStatusPaymentReq();
            statusPaymentReq.setChannel(orderRequest.getChannel());
            statusPaymentReq.setTransno(orderRequest.getTransno());
            transactionReq.setPaymentReq(statusPaymentReq);
            transactionRes.setTransactionReq(transactionReq);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (JSONException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/e-booking", method = RequestMethod.POST)
    public ResponseEntity<?> eBooking(@RequestBody EBookingReq bookingReq) {
        //something code
        try {
            CreateOrderReq orderRequest = new CreateOrderReq();
            switch (bookingReq.getChannel()) {
                case "Momo":
                    orderRequest.setChannel(PaymentChannel.Momo);
                    break;
                case "Zalopay":
                    orderRequest.setChannel(PaymentChannel.Zalopay);
                    break;
                case "ATM":
                    orderRequest.setChannel(PaymentChannel.ATM);
            }
            orderRequest.setTransno(getCurrentTimeString("yyMMdd") + "_" + new Date().getTime());
            Double amount = _parkingRepo.getRencostByName(bookingReq.getParkingname()) * bookingReq.getTimenumber();
            orderRequest.setAmount(amount.longValue());
            orderRequest.setStype("e-Booking");
            EPaymentRes transactionRes = _transactionServices.createOrder(orderRequest);
//_parkingRepo.getRencostByName(bookingReq.getParkingname()) * bookingReq.getTimenumber()
            //set TransactionReq
            TransactionReq transactionReq = new TransactionReq();
            transactionReq.setAmount(amount.longValue());
            transactionReq.setStype(orderRequest.getStype());
            transactionReq.setParkingname(bookingReq.getParkingname());
            CheckStatusPaymentReq statusPaymentReq = new CheckStatusPaymentReq();
            statusPaymentReq.setChannel(orderRequest.getChannel());
            statusPaymentReq.setTransno(orderRequest.getTransno());
            transactionReq.setPaymentReq(statusPaymentReq);
            transactionRes.setTransactionReq(transactionReq);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (JSONException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
    public ResponseEntity<?> checkOrderStatus(@RequestBody TransactionReq transactionReq) {
        try {
            if (_transactionServices.getbyTransNo(transactionReq.getPaymentReq().getTransno()) == null) {
                EPaymentRes transactionRes = _transactionServices.checkStatus(transactionReq.getPaymentReq());
                //returnCode = 0 is success
                if (transactionRes.getReturnCode() == 1 && transactionReq.getPaymentReq().getChannel().equals(PaymentChannel.Zalopay)) {
                    _transactionServices.create(transactionReq, 0);
                    transactionRes.setReturnCode(0);
                } else if (transactionRes.getReturnCode() == 1 && transactionReq.getPaymentReq().getChannel().equals(PaymentChannel.ATM)) {
                    transactionRes.setReturnCode(0);
                    _transactionServices.create(transactionReq, 0);
                } else {
                    _transactionServices.create(transactionReq, transactionRes.getReturnCode());
                }
                return new ResponseEntity(transactionRes, HttpStatus.OK);
            } else {
                return new ResponseEntity("giao dich da thanh toan", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/createTransaction", method = RequestMethod.POST)
    public ResponseEntity<?> createTransaction(@RequestBody TransactionReq transactionReq) {
        String s = null;
        try {
            //Dont checkstatus
            if (_transactionServices.getbyTransNo(transactionReq.getPaymentReq().getTransno()) != null) {
                s = "issaved";
                return new ResponseEntity(s, HttpStatus.BAD_REQUEST);
            }            
            if (_transactionServices.create(transactionReq, 0) != null) {
                s = "success";
                return new ResponseEntity(s, HttpStatus.OK);
            }
            return new ResponseEntity(s , HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/list-transaction", method = RequestMethod.GET)
    public ResponseEntity<?> listTransaction(@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageTransactionRes pageTransactionRes = _transactionServices.findAll(page, size); // fisrt page = 0
            return new ResponseEntity(pageTransactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user-transactions", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactionHistory(@RequestHeader("Authorization") String token, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            PageTransactionRes transactionRes = _transactionServices.getByUserName(username, page, size);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/user-transactions/search", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactionHistory(@RequestHeader("Authorization") String token,
            @RequestParam("from-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam("page")int page, @RequestParam("size") int size) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            PageTransactionRes transactionRes = _transactionServices.getByUserNameSearchDate(username, fromDate, toDate, page, size);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/all-transactions/search", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTransactionHistorySearch(
            @RequestParam("from-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam("page")int page, @RequestParam("size") int size) {
        try {
            PageTransactionRes transactionRes = _transactionServices.getAllSearch(fromDate, toDate, page, size);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/list-all-transaction", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTransactionHistoryByUser(@RequestHeader("Authorization") String token) {
        String username = _jwtTokenUtil.extracUsername(token.substring(7));
        List<TransactionRes> res = _transactionServices.getAllByUser(username);
        return new ResponseEntity(res, HttpStatus.OK);
    }
    @RequestMapping(value = "/list-all-transaction/search", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTransactionHistoryByUserSearch(@RequestHeader("Authorization") String token,
            @RequestParam("from-date")String fromDate, @RequestParam("to-date")String toDate) {
        String username = _jwtTokenUtil.extracUsername(token.substring(7));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate lcFromDate = LocalDate.parse(fromDate, formatter);
        LocalDate lcToDate = LocalDate.parse(toDate, formatter);
        
        List<TransactionRes> res = _transactionServices.getAllByUserSearch(username, lcFromDate, lcToDate);
        return new ResponseEntity(res, HttpStatus.OK);
    }
}
