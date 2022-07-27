/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.CheckStatusPaymentReq;
import fpt.aptech.ParkingApi.dto.request.CreateOrderReq;
import fpt.aptech.ParkingApi.dto.request.TransactionReq;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.dto.response.EPaymentRes;
import fpt.aptech.ParkingApi.entities.Transactioninformation;
import java.time.LocalDate;

/**
 *
 * @author PCvinhvizg
 */
public interface ITransaction {

    EPaymentRes createOrder(CreateOrderReq orderRequest);

    EPaymentRes checkStatus(CheckStatusPaymentReq checkStatusReq);

    Transactioninformation create(TransactionReq transactionReq, int statuscode);

    PageTransactionRes findAll(int page, int size);

    PageTransactionRes getByUserName(String username, int page, int size);

    PageTransactionRes getByUserNameSearchDate(String username, LocalDate formDate, LocalDate toDate,
            int page, int size);

    Transactioninformation getbyTransNo(String transno);

    PageTransactionRes getAllSearch(LocalDate formDate, LocalDate toDate, int page, int size);
}
