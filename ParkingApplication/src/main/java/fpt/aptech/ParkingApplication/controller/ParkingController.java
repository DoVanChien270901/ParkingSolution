/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.response.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author CHIEN
 */
@Controller
public class ParkingController {
    
    @Value("${spring.data.rest.base-path}")
    private String PATH_API;
    
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
    @RequestMapping(value = "/u/parking-history", method = RequestMethod.GET)
    public String getParkingHistory(@RequestParam("page")int currentPage, Model model, HttpSession session) {
        try {
            if (1 > currentPage) {
                currentPage = 1;
            }
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = RestTemplateConfiguration.setRequest(token);
            ResponseEntity<?> response = RestTemplateConfiguration.excuteRequest(PATH_API + "parking-history?page=" + (currentPage - 1) + "&size=10", HttpMethod.GET, request, PageParkingHistoryRes.class);
            PageParkingHistoryRes pageParkingHistoryRes = (PageParkingHistoryRes) response.getBody();
            List<ParkingHistoryRes> parkingHistoryRes = pageParkingHistoryRes.getListParkingHistory();
            if (currentPage > pageParkingHistoryRes.getTotalPages()) {
                currentPage = pageParkingHistoryRes.getTotalPages();
            }
            model.addAttribute("current", currentPage);
            int[] nav = new int[pageParkingHistoryRes.getTotalPages()];
            for (int i = 0; i <= (pageParkingHistoryRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("pageList", nav);
            model.addAttribute("parkinghistorylist", parkingHistoryRes);
            return "user/parking-history";
        } catch (Exception e) {
            return "badrequest";
        }
    }
    
    
    @RequestMapping(value = "/u/parking-history/search", method = RequestMethod.GET)
    public String parkingHistorySearch(@RequestParam("from-date") String fromDate, @RequestParam("to-date") String toDate, Model model, HttpSession session) {
        try {
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                return "redirect:/u/parking-history?page=0";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromLocalDate = LocalDate.parse(fromDate, formatter);
            LocalDate toLocalDate = LocalDate.parse(toDate, formatter);
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = RestTemplateConfiguration.setRequest(token);
            ResponseEntity<?> response = RestTemplateConfiguration.excuteRequest(PATH_API + "parking-history/search?from-date=" + fromLocalDate + "&to-date=" + toLocalDate + "&page=0&size=100", HttpMethod.GET, request, PageParkingHistoryRes.class);
            PageParkingHistoryRes pageParkingHistoryRes = (PageParkingHistoryRes) response.getBody();
            List<ParkingHistoryRes> parkingHistoryRes = pageParkingHistoryRes.getListParkingHistory();
            int[] nav = new int[pageParkingHistoryRes.getTotalPages()];
            for (int i = 0; i <= (pageParkingHistoryRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
             model.addAttribute("current", pageParkingHistoryRes.getCurrentPage());
            model.addAttribute("pageList", nav);
            model.addAttribute("parkinghistorylist", parkingHistoryRes);
            return "user/parking-history";
        } catch (Exception e) {
            return "badrequest";
        }
    }
    @RequestMapping(value = "/a/parking-history", method = RequestMethod.GET)
    public String parkingHistory(Model model) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
            ResponseEntity<?> response = RestTemplateConfiguration.excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
            ParkingRes[] parkingRes = (ParkingRes[]) response.getBody();
            model.addAttribute("lParkingRes", parkingRes);
            
            return "admin/parking-history";
    }
}
