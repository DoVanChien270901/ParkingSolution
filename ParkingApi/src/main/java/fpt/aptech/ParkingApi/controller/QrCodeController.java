/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.qrcontent.BookingQrContent;
import fpt.aptech.ParkingApi.dto.qrcontent.ProfileQrContent;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.dto.qrcontent.RechargeQrContent;
import fpt.aptech.ParkingApi.dto.request.BookingReq;
import fpt.aptech.ParkingApi.dto.request.ScanQrCodeReq;
import fpt.aptech.ParkingApi.dto.response.ScanQrCodeBookingRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.implementations.QrCodeService;
import fpt.aptech.ParkingApi.interfaces.IBooking;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vantu
 */
@RestController
@RequestMapping("/qr-code")
public class QrCodeController {

    @Autowired
    private IQrCode _qrCodeService;
    @Autowired
    private IProfile _profileService;
    @Autowired
    private ModelMapperUtil _modelMapper;
    @Autowired
    private QrCodeUtil _qrCodeUtil;
    @Autowired
    private IBooking _bookingService;
    @Autowired
    private JwtUtil _jwtTokenUtil;
    @Autowired
    private ModelMapperUtil _mapper;

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseEntity<?> createQrCode(@RequestBody RechargeByQrCodeReq rechargeByQrCodeReq, @RequestHeader("Authenticate") String token) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            Profile profile = new Profile();
            profile.setUsername(username);
            Qrcode qrcode = new Qrcode();
            qrcode.setTitle("ReChargeWallet");
            qrcode.setCreatedate(LocalDateTime.now());
            qrcode.setAccountid(profile);
            rechargeByQrCodeReq.setUsername(username);
            _qrCodeService.create(qrcode, rechargeByQrCodeReq);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public ResponseEntity<?> booking(@RequestBody BookingReq bookingReq, @RequestHeader("Authenticate") String token) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/scan-recharge", method = RequestMethod.POST)
    public ResponseEntity<?> scanQrCodeRecharge(@RequestBody ScanQrCodeReq scanQrCodeReq) {
        boolean result = false;
        try {
            result = _qrCodeService.RechargeByQrContent(scanQrCodeReq);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (result) {
            return ResponseEntity.ok("Update Balence Successful!");
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/content-recharge", method = RequestMethod.POST)
    public ResponseEntity<?> contentRecharge(@RequestBody ScanQrCodeReq scanQrCodeReq) {
        RechargeQrContent rechargeQrContent = _mapper.map(_qrCodeService.getContent(scanQrCodeReq), RechargeQrContent.class);
        if (rechargeQrContent.getUsername() != null && rechargeQrContent.getAmount() >= 10000) {
            return ResponseEntity.ok(rechargeQrContent);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/scan-booking", method = RequestMethod.POST)
    public ResponseEntity<?> scanQrCodeBooking(@RequestBody ScanQrCodeReq scanQrCodeReq) {
        boolean result = _qrCodeService.scanQrCodeBooking(scanQrCodeReq);
        if (result) {
            return ResponseEntity.ok("Successful!");
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/content-booking", method = RequestMethod.POST)
    public ResponseEntity<?> contentBooking(@RequestBody ScanQrCodeReq scanQrCodeReq) {
        BookingQrContent bookingQrContent = _mapper.map(_qrCodeService.getContent(scanQrCodeReq), BookingQrContent.class);
        if (bookingQrContent.getUsername() != null && bookingQrContent.getBookingid() != 0) {
            ScanQrCodeBookingRes res = _bookingService.getBookingById(bookingQrContent.getBookingid());
            return ResponseEntity.ok(res);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/generated/payment", method = RequestMethod.POST)
    public ResponseEntity<?> generatedQRCodePayment(@RequestHeader("Authorization") String token, @RequestParam("amount") double amount) {
        String username = _jwtTokenUtil.extracUsername(token.substring(7));
        RechargeQrContent rechargeQrContent = new RechargeQrContent();
        rechargeQrContent.setUsername(username);
        rechargeQrContent.setAmount(amount);
        byte[] res = _qrCodeUtil.generQrCode(rechargeQrContent, 650, 650);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin
    @RequestMapping(value = "/content-profile", method = RequestMethod.POST)
    public ResponseEntity<?> contentProfile(@RequestBody ScanQrCodeReq scanQrCodeReq) {
        ProfileQrContent profileQrContent = _mapper.map(_qrCodeService.getContent(scanQrCodeReq), ProfileQrContent.class);
        if (profileQrContent.getUsername() != null) {
            HashMap<String , String> res = new HashMap<String, String>();
            res.put("username", profileQrContent.getUsername());
            return ResponseEntity.ok(res);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
