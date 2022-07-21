/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.ERechargeReq;
import fpt.aptech.ParkingApplication.domain.request.BookingReq;
import fpt.aptech.ParkingApplication.domain.request.EBookingReq;
import fpt.aptech.ParkingApplication.domain.request.NewBookingReq;
import fpt.aptech.ParkingApplication.domain.response.EPaymentRes;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.PageTransactionRes;

import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.domain.response.TransactionRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 *
 * @author PCvinhvizg
 */
@Controller
public class TransactionController {

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @Autowired
    private ModelMapperUtil mapperUtil;

    @Autowired
    private RestTemplateConfiguration restTemplate;

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("attribute", "value");
        String url = "https://www.google.com/";
        return "redirect:" + url;
    }

    @RequestMapping("/pay")
    public String pay(Model model) {
        return "user/e-payment-detail";
    }

    @RequestMapping(value = "/e-payment-detail", method = RequestMethod.GET)
    public String ePaymentDetail(@RequestParam Map<String, String> allMap, Model model, HttpSession session) {
        try {
            //check requestid - Momo: requestId , Zalopay: apptransid
            String requestid = allMap.get("requestId");
            if (requestid.isEmpty()) {
                requestid = allMap.get("apptransid");
            }

            EPaymentRes orderRes = (EPaymentRes) session.getAttribute(requestid);
            HttpEntity transRequest = restTemplate.setRequest(orderRes.getTransactionReq());
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "checkStatus", HttpMethod.POST, transRequest, EPaymentRes.class);
            EPaymentRes orderResponse = (EPaymentRes) response.getBody();
            if (orderResponse.getReturnCode().equals(0) && orderRes.getTransactionReq().getStype().equals("e-Booking")) {
                // create booking
                BookingReq bookingReq = (BookingReq) session.getAttribute(orderResponse.getTransNo() + orderRes.getTransactionReq().getParkingname());
                NewBookingReq newBookingReq = new NewBookingReq(
                    orderRes.getTransactionReq().getUsername(),
                    bookingReq.getStarttime(), 
                    bookingReq.getTimenumber(), 
                    bookingReq.getCarname(),
                    bookingReq.getLisenceplates(), 
                    bookingReq.getParkingname(),
                    false
                );
                HttpEntity newbookingRequest = restTemplate.setRequest(newBookingReq);
                ResponseEntity<?> newbookingResponse = restTemplate.excuteRequest(PATH_API + "booking", HttpMethod.POST, newbookingRequest, Integer.class);
                HttpStatus status = newbookingResponse.getStatusCode();
                if (status.equals(HttpStatus.OK)) {
                    Integer id = (Integer) newbookingResponse.getBody();
                    return "redirect:/booking-details?id="+id;
                }
            } else {
                model.addAttribute("transactionReq", orderRes.getTransactionReq());
                return "user/e-payment-detail";
            }
            return "user/e-payment-detail";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/e-payment", method = RequestMethod.GET)
    public String ePayment(Model model, HttpSession session) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(token);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user", HttpMethod.GET, request, ProfileRes.class);
            ProfileRes profileRes = (ProfileRes) response.getBody();
            model.addAttribute("balance", String.valueOf(profileRes.getBalance()));
            model.addAttribute("rechargeReq", new ERechargeReq());
            return "user/e-payment";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/e-payment", method = RequestMethod.POST)
    public String createEPayment(@ModelAttribute("rechargeReq") ERechargeReq rechargeReq, HttpSession session) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");

            HttpEntity request = restTemplate.setRequest(rechargeReq);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "e-recharge", HttpMethod.POST, request, EPaymentRes.class);
            EPaymentRes orderRes = (EPaymentRes) response.getBody();
            orderRes.getTransactionReq().setUsername(loginRes.getUsername());
            session.setAttribute(orderRes.getTransNo(), orderRes);
            String url = orderRes.getPayUrl();
            return "redirect:" + url;
//            return null;
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String userTransactions(Model model, HttpSession session) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(token);

            int page = 5;
            int size = 10;

            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user-transactions?page=" + page + "&size=" + size, HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            model.addAttribute("transactionlist", usertransactions);
            return "user/history";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "badrequest";
        }
    }

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    public String createEBooking(Model model, HttpSession session) {
        try {
            model.addAttribute("bookingReq", new BookingReq());
            return "user/booking";
//            return null;
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public String createEBooking(@ModelAttribute("bookingReq") BookingReq bookingReq, HttpSession session) {
        try {
            if (bookingReq.getChannel().equals("Wallet")) {
                HttpEntity request = restTemplate.setRequest(bookingReq);
                ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "booking", HttpMethod.POST, request, ResponseEntity.class);
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    return "user/profile";
                } else {
                    return "redirect:https://www.facebook.com/";
                }
            } else {
                LoginRes loginRes = (LoginRes) session.getAttribute("account");

                EBookingReq ebookingReq = new EBookingReq();
                ebookingReq.setChannel(bookingReq.getChannel());
                ebookingReq.setParkingname(bookingReq.getParkingname());
                ebookingReq.setTimenumber(bookingReq.getTimenumber());
                ebookingReq.setParkingname("parking1");

                HttpEntity request = restTemplate.setRequest(ebookingReq);
                ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "e-booking", HttpMethod.POST, request, EPaymentRes.class);
                EPaymentRes orderRes = (EPaymentRes) response.getBody();
                orderRes.getTransactionReq().setUsername(loginRes.getUsername());

                session.setAttribute(orderRes.getTransNo() + orderRes.getTransactionReq().getParkingname(), bookingReq);
                session.setAttribute(orderRes.getTransNo(), orderRes);

                String url = orderRes.getPayUrl();
                return "redirect:" + url;
            }
//            return null;
        } catch (Exception e) {
            return "badrequest";
        }
    }
}
