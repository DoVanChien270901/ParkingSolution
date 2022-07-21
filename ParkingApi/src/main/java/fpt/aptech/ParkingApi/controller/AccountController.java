/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.qrcontent.ProfileQrContent;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.implementations.MyUserDetailsService;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import fpt.aptech.ParkingApi.dto.request.RegisterReq;
import fpt.aptech.ParkingApi.dto.response.Roles;
import fpt.aptech.ParkingApi.dto.response.LoginRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.dto.request.EditProfileReq;
import fpt.aptech.ParkingApi.dto.request.AuthenticateReq;
import fpt.aptech.ParkingApi.dto.response.PageProfileRes;
import fpt.aptech.ParkingApi.entities.*;
import fpt.aptech.ParkingApi.interfaces.IAccount;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import java.time.LocalDateTime;
import javax.xml.ws.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author CHIEN
 */
@RestController
public class AccountController {

    @Autowired
    private AuthenticationManager _authenticationManager;
    @Autowired
    private MyUserDetailsService _userDetailsService;
    @Autowired
    private IAccount _accountService;
    @Autowired
    private IProfile _profileService;
    @Autowired
    private JwtUtil _jwtTokenUtil;
    @Autowired
    private ModelMapperUtil _mapper;
    @Autowired
    private IQrCode _qrCodeService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticate(@RequestBody AuthenticateReq authenticateRequest) throws Exception {
        try {
            _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        try {
            final UserDetails userDetails = _userDetailsService
                    .loadUserByUsername(authenticateRequest.getUsername());
            final String jwt = _jwtTokenUtil.generrateToken(userDetails);
            LoginRes res = new LoginRes();
            res.setToken(jwt);
            Object[] role = userDetails.getAuthorities().toArray();
            res.setRole(Roles.valueOf(role[0].toString()));
            ProfileRes profile = _profileService.getByUserName(authenticateRequest.getUsername());
            res.setUsername(authenticateRequest.getUsername());
            res.setFullname(profile.getFullname());
            res.setEmail(profile.getEmail());
            res.setBalance(profile.getBalance());
            return ResponseEntity.ok(res);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterReq registerRequest) {
        try {
            boolean result = _accountService.create(registerRequest);
            if (result == true) {
                Profile profile = _profileService.create(registerRequest);
                ProfileQrContent qrContent = _mapper.map(registerRequest, ProfileQrContent.class);
                Qrcode qrcode = new Qrcode();
                qrcode.setTitle("PROFILE");
                qrcode.setCreatedate(LocalDateTime.now());
                qrcode.setAccountid(profile);
                _qrCodeService.create(qrcode, qrContent);
                final UserDetails userDetails = _userDetailsService
                        .loadUserByUsername(registerRequest.getUsername());
                final String jwt = _jwtTokenUtil.generrateToken(userDetails);
                LoginRes res = new LoginRes();
                res.setToken(jwt);
                Object[] role = userDetails.getAuthorities().toArray();
                res.setUsername(registerRequest.getUsername());
                res.setRole(Roles.valueOf(role[0].toString()));
                res.setFullname(profile.getFullname());
                res.setBalance(profile.getBalance());
                res.setEmail(profile.getEmail());
                return ResponseEntity.ok(res);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
//
//    @RequestMapping(value = "/list-users", method = RequestMethod.GET)
//    public ResponseEntity<?> listusers(@RequestParam("page") int page, @RequestParam("size") int size) {
//        try {
//            PageProfileRes pageProfileRes = _profileService.findAll(page, size); // fisrt page = 0
//            return new ResponseEntity(pageProfileRes, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }

//    @RequestMapping(value = "/user", method = RequestMethod.GET)
//    public ResponseEntity<?> getUserByToken(@RequestHeader("Authorization") String token) {
//        try {
//            String username = _jwtTokenUtil.extracUsername(token.substring(7));
//            ProfileRes pres = _profileService.getByUserName(username);
//
//            return new ResponseEntity(pres, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }
//
    //test
    @CrossOrigin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> getListUser() {

        return ResponseEntity.ok("hello");
    }
//    @RequestMapping(value = "/user", method = RequestMethod.PUT)
//    public ResponseEntity editUser(@RequestBody EditProfileReq editProfileReq, @RequestHeader("Authorization") String token) {
//        try {
//            String username = _jwtTokenUtil.extracUsername(token.substring(7));
//            editProfileReq.setUsername(username);
//            _profileService.edit(editProfileReq);
//            RechargeByQrCodeReq addQrReq = new RechargeByQrCodeReq();
//            //_qrCodeService.edit(,username);
//            return new ResponseEntity(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
//        }
//    }
}
