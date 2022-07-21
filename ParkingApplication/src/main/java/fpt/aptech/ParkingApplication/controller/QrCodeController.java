/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author CHIEN
 */
@Controller
public class QrCodeController {
    @RequestMapping("scan-qrcode/recharge")
    public String scanRecharge(Model model) {
        return "handle/scan-qrcode-recharge";
    }
    @RequestMapping("scan-qrcode/booking")
    public String scanBooking(Model model) {
        return "handle/scan-qrcode-booking";
    }
    @RequestMapping("scan-qrcode/new-booking")
    public String newBooking(Model model) {
        return "handle/scan-qrcode-newbooking";
    }
}
