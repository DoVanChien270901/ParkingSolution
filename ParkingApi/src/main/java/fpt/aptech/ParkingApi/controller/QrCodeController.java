/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.dto.qrcontent.RechargeQrContent;
import fpt.aptech.ParkingApi.dto.request.BookingReq;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.entities.Qrcode;
import fpt.aptech.ParkingApi.implementations.QrCodeService;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private JwtUtil _jwtTokenUtil;

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

    @RequestMapping(value = "/qr-code-profile", method = RequestMethod.GET)
    public ResponseEntity<?> getqrCodeByToken(@RequestHeader("Authenticate") String token) {

        //String accountid = jwtTokenUtil.extracUsername(tokenReq.getToken());
        return ResponseEntity.ok("asd");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get(@RequestHeader("Authenticate") String token) {
        return ResponseEntity.ok("hello");
    }
}
