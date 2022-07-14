/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.ERechargeReq;
import fpt.aptech.ParkingApplication.domain.response.EPaymentRes;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.PageTransactionRes;

import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.domain.response.TransactionRes;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    private RestTemplateConfiguration restTemplate;

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("attribute", "value");
        String url = "https://www.google.com/";
        return "redirect:" + url;
    }

    @RequestMapping(value = "/e-payment-detail", method = RequestMethod.GET)
    public String ePaymentDetail(@RequestParam Map<String, String> allMap, Model model, HttpSession session) {
        try {
            String requestid;
            if (allMap.get("requestId").isEmpty()) {
                requestid = allMap.get("apptransid");
            } else {
                requestid = allMap.get("requestId");
            }

            EPaymentRes orderRes = (EPaymentRes) session.getAttribute(requestid);
            HttpEntity request = restTemplate.setRequest(orderRes.getTransactionReq());
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "checkStatus", HttpMethod.POST, request, EPaymentRes.class);
            EPaymentRes orderResponse = (EPaymentRes) response.getBody();
            if (orderResponse.getReturnCode().equals(0) && orderRes.getTransactionReq().getStype().equals("e-Booking")) {

                // create booking
            }
//            System.out.println(orderResponse.getReturnCode());
            //lấy session key = transNo, value = ...
            //check status với transNo = true
            //
            //nếu value.type = e-Booking => goi api booking
            return "user/e-payment";
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
            
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user-transactions?page="+page+"&size="+size, HttpMethod.GET, request, PageTransactionRes.class);
            PageTransactionRes pageTransactionRes = (PageTransactionRes) response.getBody();
            List<TransactionRes> usertransactions = pageTransactionRes.getListTransaction();
            model.addAttribute("transactionlist", usertransactions);
            return "user/history";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "badrequest";
        }
    }

}
