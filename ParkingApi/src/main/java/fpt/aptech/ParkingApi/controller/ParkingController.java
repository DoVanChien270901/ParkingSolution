/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.dto.response.PageParkingHistoryRes;
import fpt.aptech.ParkingApi.dto.response.PageTransactionRes;
import fpt.aptech.ParkingApi.dto.response.ParkingRes;
import fpt.aptech.ParkingApi.entities.Parkinglocation;
import fpt.aptech.ParkingApi.interfaces.IParking;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author CHIEN
 */
@RestController
@CrossOrigin
public class ParkingController {
    @Autowired
    private IParking _parkingService;
    @Autowired
    private JwtUtil _jwtUtil;
    @RequestMapping(value = "/list-parking", method = RequestMethod.GET)
    public ResponseEntity<?> listParking() {
        List<ParkingRes> res = _parkingService.listParking();
        return new ResponseEntity(res, HttpStatus.OK);
    }
    @RequestMapping(value = "/parking-history", method = RequestMethod.GET)
    public ResponseEntity<?> parkingHistoryByUsername(@RequestHeader("Authorization") String token, @RequestParam("page") int page, @RequestParam("size") int size) {
        String username = _jwtUtil.extracUsername(token.substring(7));
            PageParkingHistoryRes res = _parkingService.getHistoryByUserName(username, page, size);
        return new ResponseEntity(res, HttpStatus.OK);
    }
    @RequestMapping(value = "/parking-history/search", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactionHistory(@RequestHeader("Authorization") String token,
            @RequestParam("from-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("to-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam("page")int page, @RequestParam("size") int size) {
        try {
            String username = _jwtUtil.extracUsername(token.substring(7));
            PageParkingHistoryRes res = _parkingService.getHistoryByUserName(username, fromDate, toDate, page, size);
            return new ResponseEntity(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value = "/parking-history/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> parkingHistoryByName(@PathVariable("name") String nane, @RequestParam("page") int page, @RequestParam("size") int size) {
            PageParkingHistoryRes res = _parkingService.getHistory(page, size);
        return new ResponseEntity(res, HttpStatus.OK);
    }
    @RequestMapping(value = "/parking/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getParkingByName(@PathVariable("name") String nane) {
        ParkingRes res = _parkingService.getByParkingName(nane);
        return new ResponseEntity(res, HttpStatus.OK);
    }
}
