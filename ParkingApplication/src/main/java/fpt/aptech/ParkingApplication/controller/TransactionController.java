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
import fpt.aptech.ParkingApplication.domain.response.PaymentChannel;
import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
//            LoginRes loginRes = (LoginRes) session.getAttribute("account");
//            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(rechargeReq);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "e-recharge", HttpMethod.POST, request, EPaymentRes.class);
            EPaymentRes orderRes = (EPaymentRes) response.getBody();
            String url = orderRes.getPayUrl();
           return "redirect:" + url;
//            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "badrequest";
        }
    }
}
