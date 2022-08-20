/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package fpt.aptech.ParkingApi.controller;

import fpt.aptech.ParkingApi.configurations.ModelMapperConfig;
import fpt.aptech.ParkingApi.dto.enumm.Roles;
import fpt.aptech.ParkingApi.dto.enumm.TitleQrCode;
import fpt.aptech.ParkingApi.dto.qrcontent.ProfileQrContent;
import fpt.aptech.ParkingApi.dto.request.RechargeByQrCodeReq;
import fpt.aptech.ParkingApi.dto.request.EditProfileReq;
import fpt.aptech.ParkingApi.dto.response.ItemPageProfile;
import fpt.aptech.ParkingApi.dto.response.PageProfileRes;
import fpt.aptech.ParkingApi.dto.response.UserDetailsRes;
import fpt.aptech.ParkingApi.dto.response.ProfileRes;
import fpt.aptech.ParkingApi.entities.Profile;
import fpt.aptech.ParkingApi.interfaces.IProfile;
import fpt.aptech.ParkingApi.interfaces.IQrCode;
import fpt.aptech.ParkingApi.repositorys.ProfileRepo;
import fpt.aptech.ParkingApi.utils.JwtUtil;
import fpt.aptech.ParkingApi.utils.ModelMapperUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author CHIEN
 */
@RestController
public class ProfileController {

    @Autowired
    private IProfile _profileService;
    @Autowired
    private IQrCode _qrCodeService;
    @Autowired
    private JwtUtil _jwtTokenUtil;
    @Autowired
    private ModelMapperUtil _mapper;
    @Autowired
    private ProfileRepo _ProfileRepo;

    @RequestMapping(value = "/list-profile", method = RequestMethod.GET)
    public ResponseEntity<?> listProfile(@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageProfileRes pageProfileRes = _profileService.findAll(page, size); // fisrt page = 0
            return new ResponseEntity(pageProfileRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/list-users", method = RequestMethod.GET)
    public ResponseEntity<?> listusers(@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageProfileRes pageProfileRes = _profileService.getListUserByRole("user", page, size); // fisrt page = 0
            return new ResponseEntity(pageProfileRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/list-users/search", method = RequestMethod.GET)
    public ResponseEntity<?> listusersByUserName(@RequestParam("name") String name, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            PageProfileRes pageProfileRes = _profileService.getListUserByRole("user", name, page, size); // fisrt page = 0
            return new ResponseEntity(pageProfileRes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

//    @RequestMapping(value = "/list-users", method = RequestMethod.GET)
//    public ResponseEntity<?> listusers(@RequestParam("page") int page, @RequestParam("size") int size) {
//        try {
//            PageProfileRes pageProfileRes = _profileService.getListUser(page, size); // fisrt page = 0
//            return new ResponseEntity(pageProfileRes, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByToken(@RequestHeader("Authorization") String token) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            ProfileRes pres = _profileService.getByUserName(username);

            return new ResponseEntity(pres, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/qrcontent", method = RequestMethod.GET)
    public ResponseEntity<?> getQrContentByUserName(@RequestParam("username") String username) {
        try {
            ProfileRes pres = _profileService.getByUserName(username);
            ProfileQrContent qrContent = _mapper.map(pres, ProfileQrContent.class);
            return new ResponseEntity(qrContent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity editUser(@RequestBody EditProfileReq editProfileReq, @RequestHeader("Authorization") String token) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));

            boolean result = _profileService.edit(editProfileReq, username);
            if (result) {
                ProfileQrContent profileQrContent = _mapper.map(editProfileReq, ProfileQrContent.class);
                _qrCodeService.edit(profileQrContent, username, TitleQrCode.PROFILE.toString());
                return new ResponseEntity("Successful", HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/user-details", method = RequestMethod.GET)
    public ResponseEntity<?> userDetails(@Param("username")String username) {
        try {
            UserDetailsRes pres = _profileService.profileDetailsByUsername(username);
            return new ResponseEntity(pres, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
//    @RequestMapping(value = "/callproc", method = RequestMethod.GET)
//    public ResponseEntity<?> abc(@RequestParam("page") int page, @RequestParam("size") int size) {
//        List<ItemPageProfile> list = _ProfileRepo.getUserByRole("user");
//        PagedListHolder holder = new PagedListHolder(list);
//        holder.setPageSize(size);
//        holder.setPage(page);
//        List<ItemPageProfile> l = holder.getPageList();
//        return null;
//    }
    @RequestMapping(value = "/get-blance", method = RequestMethod.GET)
    public ResponseEntity<?> userBlance(@RequestHeader("Authorization")String token) {
        try {
            String username = _jwtTokenUtil.extracUsername(token.substring(7));
            Double res = _profileService.getBalance(username);
            return new ResponseEntity(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
