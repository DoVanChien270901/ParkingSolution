/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.ContentQrCodeForRecharge;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.utils.QrCodeUtil;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author CHIEN
 */
@Controller
public class PaymentController {

    @Autowired
    private QrCodeUtil _qrCodeUtil;
    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @RequestMapping("/d-payment")
    public String dpayment(Model model, HttpSession session) {
        LoginRes loginRes = (LoginRes) session.getAttribute("account");
        model.addAttribute("balance", String.valueOf(loginRes.getBalance()));
        return "user/d-payment";
    }

    @RequestMapping("generator-qrcode")
    public String generatorQrcode(@RequestParam(value = "amount", required = true) double amount,
        HttpSession session, Model model) {
        LoginRes account = (LoginRes) session.getAttribute("account");
        HttpEntity request = RestTemplateConfiguration.setRequest(account.getToken());
        ResponseEntity<?> response = RestTemplateConfiguration.excuteRequest(PATH_API + "qr-code/generated/payment?amount=" + amount, HttpMethod.POST, request, byte[].class);
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(new String(Base64.getEncoder().encode((byte[]) response.getBody())));
        sb.toString();
        model.addAttribute("displaycode", sb.toString());
        model.addAttribute("balance", String.valueOf(account.getBalance()));
        return "user/d-payment";
    }

    @RequestMapping("/e-payment")
    public String epayment() {
        return "user/e-payment";
    }

    @RequestMapping("/history")
    public String history() {
        return "user/history";
    }
}
