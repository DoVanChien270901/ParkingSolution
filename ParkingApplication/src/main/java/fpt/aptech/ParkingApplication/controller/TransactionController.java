/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.CreateOrderReq;
import fpt.aptech.ParkingApplication.domain.response.CreateOrderRes;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.PaymentChannel;
import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
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

/**
 *
 * @author PCvinhvizg
 */
@Controller
public class TransactionController {

    @Autowired
    private ModelMapperUtil _modelmapper;

    @Autowired
    private RestTemplateConfiguration restTemplate;

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @RequestMapping("/url")
    public String page(Model model) {
        model.addAttribute("attribute", "value");
        return "view.name";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("attribute", "value");
        String url = "https://www.google.com/";
        return "redirect:" + url;
    }

//    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
//    public String getUser(Model model, HttpSession session) {
//        try {
//            LoginRes loginRes = (LoginRes) session.getAttribute("account");
//            String token = loginRes.getToken();
//            HttpEntity request = restTemplate.setRequest(token);
//            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user", HttpMethod.GET, request, ProfileRes.class);
//            ProfileRes profileRes = (ProfileRes) response.getBody();
//            Double balance = profileRes.getBalance();
//            model.addAttribute("balance", String.valueOf(balance));
//            return "user/profile";
//        } catch (Exception e) {
//            return "badrequest";
//        }
//    }
//
//    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
//    public String getUser(@ModelAttribute("orderReq") CreateOrderReq orderReq, HttpSession session) {
//        try {
//            LoginRes loginRes = (LoginRes) session.getAttribute("account");
//            String token = loginRes.getToken();
//            HttpEntity getUsernameRequest = restTemplate.setRequest(token);
//            ResponseEntity<?> getusername = restTemplate.excuteRequest(PATH_API + "username", HttpMethod.GET, getUsernameRequest, String.class);
//
//            String username = (String) getusername.getBody();
//            orderReq.setUsername(username);
//
//            //test
//            orderReq.setChannel(PaymentChannel.Momo);
//
//            HttpEntity request = restTemplate.setRequest(orderReq);
//            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "recharge", HttpMethod.GET, request, CreateOrderReq.class);
//            CreateOrderRes createOrderRes = (CreateOrderRes) response.getBody();
//            String url = createOrderRes.getPayUrl();
//            return "redirect:" + url;
//        } catch (Exception e) {
//            return "badrequest";
//        }
//    }
}
