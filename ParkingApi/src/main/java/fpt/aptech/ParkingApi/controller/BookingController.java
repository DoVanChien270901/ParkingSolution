/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.qrcontent.BookingQrContent;
import fpt.aptech.ParkingApi.dto.request.NewBookingReq;
import fpt.aptech.ParkingApi.dto.response.BookingDetailRes;
import fpt.aptech.ParkingApi.dto.response.BookingRes;
import fpt.aptech.ParkingApi.dto.response.ListBookingRes;
import fpt.aptech.ParkingApi.entities.Booking;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.interfaces.IBooking;
import fpt.aptech.ParkingApi.interfaces.IParking;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author CHIEN
 */
@RestController
public class BookingController {

    @Autowired
    private IProfile _profileServices;
    @Autowired
    private IQrCode _qrcodeService;
    @Autowired
    private IBooking _bookingService;
    @Autowired
    private JwtUtil _jwtUtil;

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public ResponseEntity<?> newBooking(@RequestBody NewBookingReq bookingReq) {
        //String username = _jwtUtil.extracUsername(token.substring(7));
        String username = bookingReq.getUsername();
        if (bookingReq.isWalletparking()) {
            boolean result = _profileServices.deductionBalanceForBooking(bookingReq.getTimenumber(), username, bookingReq.getParkingname());
            if (!result) {
                return new ResponseEntity("Insufficient balance", HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        Booking booking = _bookingService.create(bookingReq, username);
        //create qrcode
        Qrcode qrcode = new Qrcode();
        qrcode.setTitle(TitleQrCode.BOOKING.toString());
        qrcode.setCreatedate(LocalDateTime.now());
        qrcode.setAccountid(new Profile(username));
        BookingQrContent bookingQrContent = new BookingQrContent();
        bookingQrContent.setUsername(username);
        bookingQrContent.setBookingid(booking.getId());
        _qrcodeService.create(qrcode, bookingQrContent);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/list-booking", method = RequestMethod.GET)
    public ResponseEntity<?> listBooking(@RequestHeader("Authorization") String token) {
        String username = _jwtUtil.extracUsername(token.substring(7));
        List<BookingRes> listBooking = _bookingService.getListBookingByUsername(username);
        ListBookingRes res = new ListBookingRes(listBooking);
        return new ResponseEntity(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/booking-details", method = RequestMethod.GET)
    public ResponseEntity<?> bookingDetails(@RequestParam("id") int id, @RequestHeader("Authorization") String token) {
        String username = _jwtUtil.extracUsername(token.substring(7));
        BookingDetailRes res = _bookingService.getDetailBookingById(id, username);
        return new ResponseEntity(res, HttpStatus.OK);
    }
}
