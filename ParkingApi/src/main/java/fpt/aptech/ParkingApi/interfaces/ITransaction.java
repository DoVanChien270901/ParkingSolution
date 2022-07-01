/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.interfaces;

import fpt.aptech.ParkingApi.dto.request.OrderReq;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PCvinhvizg
 */
public interface ITransaction {
    TransactionRes createOrder(OrderReq orderRequest);
//    Map<String, Object> createOrder(OrderReq orderRequest);
}
