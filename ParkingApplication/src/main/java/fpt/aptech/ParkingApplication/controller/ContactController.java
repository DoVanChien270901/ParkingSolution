/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.aptech.ParkingApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author asus
 */
@Controller
public class ContactController {
    
    @RequestMapping("/contact")
    public String contact(Model model) {
        //model.addAttribute("attribute", "value");
        return "user/contact";
    }
    
}
