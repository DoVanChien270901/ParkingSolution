/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.CreateOrderReq;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.dto.response.CreateOrderRes;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;

/**
 *
 * @author PCvinhvizg
 */
public interface ITransaction {
    CreateOrderRes createOrder(CreateOrderReq orderRequest);
    CreateOrderRes checkStatus(CreateOrderReq orderRequest);
    PageTransactionRes findAll(int page, int size);
    TransactionRes getByUserName(String username);
}
