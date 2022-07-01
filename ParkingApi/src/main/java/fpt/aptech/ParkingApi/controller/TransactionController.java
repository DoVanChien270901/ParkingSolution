/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.request.OrderReq;
import fpt.aptech.ParkingApi.dto.response.TransactionRes;
import fpt.aptech.ParkingApi.interfaces.ITransaction;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "createOrder", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody OrderReq orderRequest) {
        //something code
        try {
            TransactionRes transactionRes = _transactionServices.createOrder(orderRequest);
            return new ResponseEntity(transactionRes, HttpStatus.OK);
        } catch (JSONException e) {
            
            String message = e.getMessage();
            
            
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
