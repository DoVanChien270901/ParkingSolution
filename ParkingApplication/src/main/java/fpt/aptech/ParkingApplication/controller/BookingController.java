/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.EditProfileReq;
import fpt.aptech.ParkingApplication.domain.response.BookingDetailRes;
import fpt.aptech.ParkingApplication.domain.response.BookingRes;
import fpt.aptech.ParkingApplication.domain.response.ListBookingRes;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author CHIEN
 */
@Controller
public class BookingController {

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @RequestMapping(value = "booking-details", method = RequestMethod.GET)
    public String test(@RequestParam("id") int id, HttpSession session, Model model) {
        LoginRes loginRes = (LoginRes) session.getAttribute("account");
        String token = loginRes.getToken();
        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
            .excuteRequest(PATH_API + "booking-details?id=" + id, HttpMethod.GET, request, BookingDetailRes.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            BookingDetailRes bookingDetailRes = (BookingDetailRes) response.getBody();
            //display qrcode
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(new String(Base64.getEncoder().encode(bookingDetailRes.getQrcontent())));
            sb.toString();
            model.addAttribute("displaycode", sb.toString());
            model.addAttribute("bookingDetailRes", bookingDetailRes);
            return "user/qrcode-booking";
        } else {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/list-booking", method = RequestMethod.GET)
    public String listBooking(HttpSession session, Model model) {
        LoginRes loginRes = (LoginRes) session.getAttribute("account");
        String token = loginRes.getToken();
        HttpEntity request = RestTemplateConfiguration.setRequest(token);
        ResponseEntity<?> response = RestTemplateConfiguration
            .excuteRequest(PATH_API + "list-booking", HttpMethod.GET, request, ListBookingRes.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ListBookingRes bookingRes = (ListBookingRes) response.getBody();
            model.addAttribute("listBookingRes", bookingRes.getBookingRes());
            return "user/qrcode-booking";
        } else {
            return "badrequest";
        }
    }
    @RequestMapping(value = "/new-booking", method = RequestMethod.GET)
    public String newBooking(@RequestParam("username") String username) {
        return "handle/booking";
    }
}
