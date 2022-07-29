/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.interfaces.IRevenue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author CHIEN
 */
@RestController
public class RevenueController {
    @Autowired
    private IRevenue _revenueServices;
    @RequestMapping(value = "/data/revenue-month", method = RequestMethod.GET)
    public ResponseEntity<?> revenueMonth(@RequestParam("year")int year){
        return new ResponseEntity<>( _revenueServices.byMonths(year),HttpStatus.OK);
    }
    @RequestMapping(value = "/data/revenue-day", method = RequestMethod.GET)
    public ResponseEntity<?> revenueMonth(@RequestParam("month")int month, @RequestParam("year")int year){
        return new ResponseEntity<>( _revenueServices.byDays(month, year),HttpStatus.OK);
    }
}
