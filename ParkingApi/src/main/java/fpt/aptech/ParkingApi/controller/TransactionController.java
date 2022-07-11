/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.PaymentChannel;
import fpt.aptech.ParkingApi.dto.request.CheckStatusPaymentReq;
import fpt.aptech.ParkingApi.dto.request.CreateOrderReq;
import fpt.aptech.ParkingApi.dto.request.ERechargeReq;
import fpt.aptech.ParkingApi.dto.request.TransactionReq;
import fpt.aptech.ParkingApi.dto.response.EPaymentRes;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.interfaces.ITransaction;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
    private ITransaction _transactionServices;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
        return "Test";
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
            orderRequest.setTransno(String.valueOf(System.currentTimeMillis()));
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

    //doit
    @RequestMapping(value = "/e-booking", method = RequestMethod.POST)
    public ResponseEntity<?> eBooking(@RequestBody ERechargeReq rechargeReq) {
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
            orderRequest.setTransno(String.valueOf(System.currentTimeMillis()));
            orderRequest.setAmount(rechargeReq.getAmount());
            orderRequest.setStype("e-Booking");
            EPaymentRes transactionRes = _transactionServices.createOrder(orderRequest);
            
            //set TransactionReq
            TransactionReq transactionReq = new TransactionReq();
            transactionReq.setAmount(rechargeReq.getAmount());
            transactionReq.setStype(orderRequest.getStype());
            CheckStatusPaymentReq statusPaymentReq = new CheckStatusPaymentReq();
            statusPaymentReq.setChannel(orderRequest.getChannel());
            statusPaymentReq.setTransno(orderRequest.getTransno());
            transactionReq.setPaymentReq(statusPaymentReq);
            transactionRes.setTransactionReq(transactionReq);
            //thiếu set booking name
            
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

    @RequestMapping(value = "/list-transaction", method = RequestMethod.GET)
    public ResponseEntity<?> listTransaction(@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageTransactionRes pageTransactionRes = _transactionServices.findAll(page, size); // fisrt page = 0
            return new ResponseEntity(pageTransactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user-transactions-history", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactionHistory(@RequestParam("username") String username, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageTransactionRes transactionRes = _transactionServices.getByUserName(username, page, size);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
