/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author CHIEN
 */
@Controller
public class ParkingController {
    
    @RequestMapping("/list-parking")
    public String listParking() {
        return "admin/parking-manager";
    }
    @RequestMapping("/parking-history")
    public String history() {
        return "admin/parking-history";
    }
    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map() {
        return "user/google-map";
    }
    
}
