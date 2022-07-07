/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package fpt.aptech.ParkingApplication.controller;

import fpt.aptech.ParkingApplication.configuration.RestTemplateConfiguration;
import fpt.aptech.ParkingApplication.domain.request.EditProfileReq;
import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.ProfileRes;
import fpt.aptech.ParkingApplication.utils.ModelMapperUtil;
import java.util.Base64;
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
//            StringBuilder sb = new StringBuilder();
//            sb.append("data:image/png;base64,");
//            sb.append(new String(Base64.getEncoder().encode(profileRes.getQrContent())));
//            sb.toString();
//
//            model.addAttribute("displaycode", sb.toString());
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
                session.setAttribute("account" ,loginRes);
                return "redirect:/profile";
            } else {
                return "badrequest";
            }
        } catch (Exception e) {
            return "badrequest";
        }
    }
}
