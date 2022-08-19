/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fpt.aptech.ParkingApplication.configuration;

import fpt.aptech.ParkingApplication.domain.response.LoginRes;
import fpt.aptech.ParkingApplication.domain.response.Roles;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

/**
 *
 * @author CHIEN
 */
@Component
public class FilterConfig implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        LoginRes token;
        try {
            token = (LoginRes)request.getSession().getAttribute("account");
            if (token.getRole() != Roles.user) {
                token = null;
            }
        } catch (Exception e) {
             token = null;
        }
        if (token==null) {
            response.sendRedirect("/account/login");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}