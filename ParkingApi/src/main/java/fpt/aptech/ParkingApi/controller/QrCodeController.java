/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.request.AddQrReq;
import fpt.aptech.ParkingApi.dto.qrcontent.AddRechargeQrConten;
import fpt.aptech.ParkingApi.implementations.QrCodeService;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.utils.QrCodeUtil;
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
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private ModelMapperUtil modelMapper;
    @Autowired
    private QrCodeUtil qrCodeUtil;
    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping(value = "/qr-code", method = RequestMethod.POST)
    public ResponseEntity createQrCode(@RequestBody AddRechargeQrConten rechargeReq, @RequestHeader("Authenticate") String token) {
        try {
            String username = jwtTokenUtil.extracUsername(token.substring(7));
            AddQrReq addQrReq = new AddQrReq();
            addQrReq.setContent(rechargeReq);
            addQrReq.setTitle(TitleQrCode.RECHARMONEY);
            qrCodeService.create(addQrReq, username);
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
}
