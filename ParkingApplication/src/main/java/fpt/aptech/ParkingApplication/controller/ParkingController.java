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
public class ParkingController {
    
    @RequestMapping("/list-parking")
    public String listParking() {
        return "admin/parking-manager";
    }
    @RequestMapping("/parking-history")
    public String history() {
        return "admin/parking-history";
    }
    
}
