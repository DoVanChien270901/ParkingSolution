/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.EditProfileReq;
import fpt.aptech.ParkingApplication.domain.response.ItemPageProfile;
import fpt.aptech.ParkingApplication.domain.response.ListPage;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.PageProfileRes;
import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.domain.response.UserDetailsRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author CHIEN
 */
@Controller
public class ProfileController {

    @Autowired
    private ModelMapperUtil _modelmapper;

    @Value("${spring.data.rest.base-path}")
    private String PATH_API;

    @Autowired
    private RestTemplateConfiguration restTemplate;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String page(Model model) {
        model.addAttribute("attribute", "value");
        return "view.name";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getUser(Model model, HttpSession session) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            HttpEntity request = restTemplate.setRequest(token);
            ResponseEntity<?> response = restTemplate.excuteRequest(PATH_API + "user", HttpMethod.GET, request, ProfileRes.class);
            ProfileRes profileRes = (ProfileRes) response.getBody();
//display qrcode
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(new String(Base64.getEncoder().encode(profileRes.getQrcontent())));
            sb.toString();
            model.addAttribute("displaycode", sb.toString());
            model.addAttribute("profile", profileRes);
            return "user/profile";
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String getUser(@ModelAttribute("profile") ProfileRes profileRes, HttpSession session) {
        try {
            LoginRes loginRes = (LoginRes) session.getAttribute("account");
            String token = loginRes.getToken();
            EditProfileReq editProfileReq = _modelmapper.map(profileRes, EditProfileReq.class);
            HttpEntity request = restTemplate.setRequest(token, editProfileReq);
            ResponseEntity<?> response = restTemplate
                    .excuteRequest(PATH_API + "user", HttpMethod.PUT, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                loginRes.setFullname(profileRes.getFullname());
                session.setAttribute("account", loginRes);
                return "redirect:/profile";
            } else {
                return "badrequest";
            }
        } catch (Exception e) {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/list-user", method = RequestMethod.GET)
    public String listUser(@RequestParam("page") int currentPage, Model model) {
        if (1 > currentPage) {
            currentPage = 1;
        }
        HttpEntity request = restTemplate.setRequest();
        ResponseEntity<?> response = restTemplate
                .excuteRequest(PATH_API + "list-users?page=" + (currentPage - 1) + "&size=10", HttpMethod.GET, request, PageProfileRes.class);
        PageProfileRes pageProfileRes = (PageProfileRes) response.getBody();
        model.addAttribute("listProfile", pageProfileRes.getListProfile());
        if (currentPage > pageProfileRes.getTotalPages()) {
            currentPage = pageProfileRes.getTotalPages();
        }
        model.addAttribute("current", currentPage);
        int[] nav = new int[pageProfileRes.getTotalPages()];
        for (int i = 0; i <= (pageProfileRes.getTotalPages()-1); i++) {
            nav[i] = i+1;
        }
        //int[] nav = nav(currentPage, pageProfileRes.getTotalPages());
        model.addAttribute("pageList", nav);
        return "admin/user-manager";
    }
    @RequestMapping(value = "/list-user/search", method = RequestMethod.GET)
    public String listSearchUser(@RequestParam("name")String name, Model model) {
        if (name.isEmpty()) {
            return "redirect:/list-user?page=0";
        }
        HttpEntity request = restTemplate.setRequest();
        ResponseEntity<?> response = restTemplate
                .excuteRequest(PATH_API + "list-users/search?name="+name+"&page=0" + "&size=100", HttpMethod.GET, request, PageProfileRes.class);
        PageProfileRes pageProfileRes = (PageProfileRes) response.getBody();
        model.addAttribute("listProfile", pageProfileRes.getListProfile());
        int currentPage = pageProfileRes.getCurrentPage();
        if (currentPage > pageProfileRes.getTotalPages()) {
            currentPage = pageProfileRes.getTotalPages();
        }
        model.addAttribute("current", currentPage);
        int[] nav = new int[pageProfileRes.getTotalPages()];
        for (int i = 0; i <= (pageProfileRes.getTotalPages()-1); i++) {
            nav[i] = i+1;
        }
        //int[] nav = nav(currentPage, pageProfileRes.getTotalPages());
        model.addAttribute("pageList", nav);
        return "admin/user-manager";
    }

    @RequestMapping(value = "/user-details", method = RequestMethod.GET)
    public String userDetails(@RequestParam("id") String username, @RequestParam("page") int currentPage, Model model) {
        HttpEntity request = restTemplate.setRequest();
        ResponseEntity<?> response = restTemplate
                .excuteRequest(PATH_API + "user-details?username=" + username, HttpMethod.GET, request, UserDetailsRes.class);
        UserDetailsRes detailsRes = (UserDetailsRes) response.getBody();
        model.addAttribute("userDetails", detailsRes);
        model.addAttribute("currentPage", currentPage);
        return "admin/user-manager";
    }

//    private int[] nav(int currentPage, int totalPage) {
//        int[] nav = new int[10];
//        int step = (totalPage - (currentPage + 5)) / 5; // (total - (curent+5)) : 5
//        nav[0] = currentPage;
//        if (currentPage >= (totalPage - 5)) {
//            nav[9] = totalPage;
//            nav[8] = totalPage - 1;
//            nav[7] = totalPage - 2;
//            nav[6] = totalPage - 3;
//            nav[5] = totalPage - 4;
//            currentPage = totalPage - 4;
//            step = currentPage / 5;
//            for (int j = 4; j >= 0; j--) {
//                nav[j] = currentPage - step;
//                currentPage = currentPage - step;
//            }
//        } else if (step == 0) {
//            nav = new int[(totalPage - currentPage) + 1];
//            nav[0] = currentPage;
//            for (int i = 1; i < (nav.length); i++) {
//                nav[i] = ++currentPage;
//            }
//        } else {
//            for (int i = 1; i <= 9; i++) {
//                if (i <= 4) {
//                    currentPage++;
//                } else if (i == 9) {
//                    currentPage = totalPage;
//                } else {
//                    currentPage = currentPage + step;
//                }
//                nav[i] = currentPage;
//            }
//        }
//        return nav;
//    }
}
