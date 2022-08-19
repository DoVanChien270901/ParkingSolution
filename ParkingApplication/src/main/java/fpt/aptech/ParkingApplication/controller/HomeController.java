/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author CHIEN
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/user")
    public String homeUser() {
        return "user/index";
    }

    @RequestMapping("/admin")
    public String homeAdmin() {
        return "redirect:/a/revenue/month";
    }
    
    @RequestMapping("/handle")
    public String homeHandle() {
        return "redirect:/h/status-parking/all";
    }
}
