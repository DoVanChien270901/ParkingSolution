/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.response.DataRevenueDayRes;
import fpt.aptech.ParkingApplication.domain.response.DataRevenueMonthRes;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author CHIEN
 */
@Controller
@RequestMapping("/a/revenue")
public class RevenueController {
    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @RequestMapping(value = "/month", method = RequestMethod.GET)
    public String revenueMonth(Model model, @RequestParam(value = "year", required = false,
            defaultValue = "#{T(java.time.Year).now().getValue()}") int year,
            @RequestParam(value = "type", required = false, defaultValue = "line") String type) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "data/revenue-month?year=" + year, HttpMethod.GET, request, DataRevenueMonthRes[].class);
        DataRevenueMonthRes[] dataRevenueMonthReses = (DataRevenueMonthRes[]) response.getBody();
        // create int[] year
        int currentYear = Year.now().getValue();
        int size = currentYear - 2020 + 1;
        int startYear = 2020;
        int[] arrYear = new int[size];
        for (int i = 0; i < size; i++) {
            arrYear[i] = startYear;
            startYear ++;
        }
        if (dataRevenueMonthReses.length < 1) {
            model.addAttribute("errormes", "Data not found or invalid data !");
        }
        model.addAttribute("data", dataRevenueMonthReses);
        model.addAttribute("arrYear", arrYear);
        model.addAttribute("year", year);
        model.addAttribute("type", type);
        return "admin/revenue-month";
    }
    @RequestMapping(value = "/day", method = RequestMethod.GET)
    public String revenueDay(Model model, @RequestParam(value = "monthofyear", required = false, defaultValue = "null") String monthofyear,
            @RequestParam(value = "type", required = false, defaultValue = "line") String type) {
        String year = null;
        String month = null;
        if (monthofyear.equals("null")) {
            year = String.valueOf(LocalDate.now().getYear());
            month = String.valueOf(LocalDate.now().getMonthValue());
        }else{
            year = monthofyear.substring(0,4);
            month = monthofyear.substring(5,7);
        }
        
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "data/revenue-day?month="+month+"&year="+year, HttpMethod.GET, request, DataRevenueDayRes[].class);
        DataRevenueDayRes[] dataRevenueDayRes = (DataRevenueDayRes[]) response.getBody();

        if (dataRevenueDayRes.length < 1) {
            model.addAttribute("errormes", "Data not found or invalid data !");
        }
        model.addAttribute("data", dataRevenueDayRes);
        model.addAttribute("type", type);
        model.addAttribute("monthofyear", year+"-"+month);
        model.addAttribute("type", type);
        return "admin/revenue-day";
    }
   
}
