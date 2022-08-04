/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.AddParkingReq;
import fpt.aptech.ParkingApplication.domain.request.UpdateParkingReq;
import fpt.aptech.ParkingApplication.domain.response.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author CHIEN
 */
@Controller
public class ParkingController {

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @RequestMapping("/a/list-parking")
    public String listParking(Model model) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        HttpEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
        ParkingRes[] res = (ParkingRes[]) response.getBody();
        model.addAttribute("listParking", res);
        return "admin/parking-manager";
    }

    @RequestMapping("/a/list-parking/search")
    public String listParking(@RequestParam("keyword") String key, Model model) {
        if (key.isEmpty()) {
            return "redirect:/a/list-parking";
        } else {
            HttpEntity request = RestTemplateConfiguration.setRequest();
            HttpEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "parking?search=" + key, HttpMethod.GET, request, ParkingRes[].class);
            ParkingRes[] res = (ParkingRes[]) response.getBody();
            model.addAttribute("listParking", res);
            return "admin/parking-manager";
        }
    }

    @RequestMapping(value = "/map", method = RequestMethod.GET)
    public String map(Model model) {
        HttpEntity resquest = RestTemplateConfiguration.setRequest();
        ResponseEntity response = RestTemplateConfiguration
                .excuteRequest(PATH_API+ "list-parking", HttpMethod.GET, resquest, ParkingRes[].class);
        ParkingRes[] listParking = (ParkingRes[]) response.getBody();
        model.addAttribute("listparking", listParking);
        return "user/google-map";
    }

    @RequestMapping(value = "/u/parking-history", method = RequestMethod.GET)
    public String getParkingHistory(@RequestParam("page") int currentPage, Model model, HttpSession session) {
        try {
            if (1 > currentPage) {
                currentPage = 1;
            }
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = RestTemplateConfiguration.setRequest(token);
            ResponseEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "parking-history?page=" + (currentPage - 1) + "&size=10", HttpMethod.GET, request, PageParkingHistoryRes.class);
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
            ResponseEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "parking-history/search?from-date="
                            + fromLocalDate + "&to-date=" + toLocalDate
                            + "&page=0&size=100", HttpMethod.GET, request, PageParkingHistoryRes.class);
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

    @RequestMapping(value = "/a/parking-history/{name}", method = RequestMethod.GET)
    public String parkingHistory(Model model, @PathVariable("name") String name, @RequestParam("page") int currentPage) {
        //get select box
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
        ParkingRes[] parkingRes = (ParkingRes[]) response.getBody();
        model.addAttribute("lParkingRes", parkingRes);
        //get content page
        if (1 > currentPage) {
            currentPage = 1;
        }
        PageParkingHistoryRes pageParkingHistoryRes = new PageParkingHistoryRes();
        if (name.equals("all")) {
            ResponseEntity<?> responseContent = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "all-parking-history?page=" + (currentPage - 1) + "&size=10",
                            HttpMethod.GET, request, PageParkingHistoryRes.class);
            pageParkingHistoryRes = (PageParkingHistoryRes) responseContent.getBody();
            model.addAttribute("listParkingHistory", pageParkingHistoryRes.getListParkingHistory());
        } else {
            ResponseEntity<?> responseContent = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "parking-history/" + name + "?page=" + (currentPage - 1) + "&size=10",
                            HttpMethod.GET, request, PageParkingHistoryRes.class);
            pageParkingHistoryRes = (PageParkingHistoryRes) responseContent.getBody();
            model.addAttribute("listParkingHistory", pageParkingHistoryRes.getListParkingHistory());
        }

        if (currentPage > pageParkingHistoryRes.getTotalPages()) {
            currentPage = pageParkingHistoryRes.getTotalPages();
        }
        model.addAttribute("current", currentPage);
        int[] nav = new int[pageParkingHistoryRes.getTotalPages()];
        for (int i = 0; i <= (pageParkingHistoryRes.getTotalPages() - 1); i++) {
            nav[i] = i + 1;
        }
        model.addAttribute("selected", name);
        model.addAttribute("pageList", nav);
        return "admin/parking-history";
    }

    @RequestMapping(value = "/a/parking-history/search", method = RequestMethod.GET)
    public String parkingHistoryFromParkingName(Model model,
            @RequestParam("from-date") String fromDate,
            @RequestParam("to-date") String toDate,
            @RequestParam("name") String name) {
        if (fromDate.isEmpty() || toDate.isEmpty()) {
            return "redirect:/a/parking-history/all?page=0";
        }
        //
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", fromDate);
        //get select box
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
        ParkingRes[] parkingRes = (ParkingRes[]) response.getBody();
        model.addAttribute("lParkingRes", parkingRes);
        //get content page
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromLocalDate = LocalDate.parse(fromDate, formatter);
        LocalDate toLocalDate = LocalDate.parse(toDate, formatter);
        ResponseEntity<?> responseContent = RestTemplateConfiguration
                .excuteRequest(PATH_API + "all-parking-history/search?name=" + name + "&from-date="
                        + fromDate + "&to-date=" + toDate + "&page=0" + "&size=100",
                        HttpMethod.GET, request, PageParkingHistoryRes.class);
        PageParkingHistoryRes pageParkingHistoryRes = (PageParkingHistoryRes) responseContent.getBody();
        model.addAttribute("listParkingHistory", pageParkingHistoryRes.getListParkingHistory());
        model.addAttribute("current", pageParkingHistoryRes.getCurrentPage());
        int[] nav = new int[pageParkingHistoryRes.getTotalPages()];
        for (int i = 0; i <= (pageParkingHistoryRes.getTotalPages() - 1); i++) {
            nav[i] = i + 1;
        }
        model.addAttribute("selected", name);
        model.addAttribute("pageList", nav);
        return "admin/parking-history";
    }

    @RequestMapping(value = "/a/parking-history/", method = RequestMethod.GET)
    public String parkingHistoryFromParkingName(Model model, @RequestParam("name") String name, @RequestParam("page") int currentPage) {
        //get select box
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
        ParkingRes[] parkingRes = (ParkingRes[]) response.getBody();
        model.addAttribute("lParkingRes", parkingRes);
        //get content page
        if (1 > currentPage) {
            currentPage = 1;
        }
        ResponseEntity<?> responseContent = RestTemplateConfiguration
                .excuteRequest(PATH_API + "par  king-history/" + name + "?page=" + (currentPage - 1) + "&size=10",
                        HttpMethod.GET, request, PageParkingHistoryRes.class);
        PageParkingHistoryRes pageParkingHistoryRes = (PageParkingHistoryRes) responseContent.getBody();
        model.addAttribute("listParkingHistory", pageParkingHistoryRes.getListParkingHistory());
        if (currentPage > pageParkingHistoryRes.getTotalPages()) {
            currentPage = pageParkingHistoryRes.getTotalPages();
        }
        model.addAttribute("current", currentPage);
        int[] nav = new int[pageParkingHistoryRes.getTotalPages()];
        for (int i = 0; i <= (pageParkingHistoryRes.getTotalPages() - 1); i++) {
            nav[i] = i + 1;
        }

        model.addAttribute("selected", name);
        model.addAttribute("pageList", nav);
        return "admin/parking-history";
    }

    @RequestMapping(value = "/a/parking/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("id") String id) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "parking/" + id, HttpMethod.DELETE, request, String.class);
        return "redirect:/list-parking";
    }

    @RequestMapping(value = "/a/parking/add", method = RequestMethod.GET)
    public String creatpre(@ModelAttribute("addParkingReq") AddParkingReq addParkingReq) {
        return "admin/parking-manager";
    }

    @RequestMapping(value = "/a/parking/add", method = RequestMethod.POST)
    public String createpost(@ModelAttribute("addParkingReq") AddParkingReq addParkingReq, RedirectAttributes redirectAttributes) {
        HttpEntity request = RestTemplateConfiguration.setRequest(addParkingReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "parking", HttpMethod.POST, request, String.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return "redirect:/list-parking";
        } else {
            redirectAttributes.addFlashAttribute("errormes", "Parking name is already");
            redirectAttributes.addFlashAttribute("addParkingReq", addParkingReq);
            return "redirect:/a/parking/add";
        }

    }

    @RequestMapping(value = "/a/parking/update", method = RequestMethod.GET)
    public String updatepre(@RequestParam("id") String name, Model model) {
        HttpEntity request = RestTemplateConfiguration.setRequest();
        ResponseEntity<?> response = RestTemplateConfiguration.
                excuteRequest(PATH_API + "parking/" + name, HttpMethod.GET, request, ParkingRes.class);
        ParkingRes parkingRes = (ParkingRes) response.getBody();
        model.addAttribute("updateParkingReq", parkingRes);
        return "admin/parking-manager";
    }

    @RequestMapping(value = "/a/parking/update", method = RequestMethod.POST)
    public String updatepost(@ModelAttribute("addParkingReq") UpdateParkingReq updateParkingReq, RedirectAttributes redirectAttributes) {
        HttpEntity request = RestTemplateConfiguration.setRequest(updateParkingReq);
        ResponseEntity<?> response = RestTemplateConfiguration
                .excuteRequest(PATH_API + "parking", HttpMethod.PUT, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/list-parking";
        } else {
            redirectAttributes.addFlashAttribute("errormes", "Parking name is already");
            redirectAttributes.addFlashAttribute("addParkingReq", updateParkingReq);
            return "redirect:/a/parking/add";
        }

    }

    @RequestMapping(value = "/h/status-parking/{name}", method = RequestMethod.GET)
    public String viewParkingStatus(@PathVariable("name") String name,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page, Model model) {
        if (page<1) {
            page =1;
        }
        HttpEntity request = RestTemplateConfiguration.setRequest();
        HttpEntity<?> responseListParking = RestTemplateConfiguration
                .excuteRequest(PATH_API + "list-parking", HttpMethod.GET, request, ParkingRes[].class);
        ParkingRes[] parkingRes = (ParkingRes[]) responseListParking.getBody();
        model.addAttribute("listParking", parkingRes);
        if (name.equals("all")) {
            model.addAttribute("selected", "all");
            ResponseEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "load-status-parking", HttpMethod.GET, request, LoadStatusParking[].class);
            LoadStatusParking[] loadStatusParkings = (LoadStatusParking[]) response.getBody();
            model.addAttribute("loadStatusParkings", loadStatusParkings);
            return "handle/parking-status";
        } else {
            //call get list booking
            ResponseEntity<?> responseListBooking = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "list-booking/" + name + "?page=" + page + "&size=10", HttpMethod.GET, request, PageBookingRes.class);
            PageBookingRes pageBookingRes = (PageBookingRes) responseListBooking.getBody();
            model.addAttribute("bookingRes", pageBookingRes.getListBooking());
            //
            model.addAttribute("selected", name);
            //arr panigation
            if (page > pageBookingRes.getTotalPages()) {
                page = pageBookingRes.getTotalPages();
            }
            model.addAttribute("current", page);
            int[] nav = new int[pageBookingRes.getTotalPages()];
            for (int i = 0; i <= (pageBookingRes.getTotalPages() - 1); i++) {
                nav[i] = i + 1;
            }
            model.addAttribute("pageList", nav);
            //
            ResponseEntity<?> response = RestTemplateConfiguration
                    .excuteRequest(PATH_API + "load-status-parking/" + name, HttpMethod.GET, request, LoadStatusParking.class);
            LoadStatusParking loadStatusParking = (LoadStatusParking) response.getBody();
            LoadStatusParking[] loadStatusParkings = {loadStatusParking};
            model.addAttribute("loadStatusParkings", loadStatusParkings);
            return "handle/parking-status";
        }
    }
}
