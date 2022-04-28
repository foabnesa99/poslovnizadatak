package com.example.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


@Api(value = "Home page controller", description = "REST API for the home page")
@RestController
@RequestMapping(value = "/")
@Slf4j
public class HomePageController {

    @GetMapping("/home")
    public ModelAndView homepage(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        ModelAndView mav = new ModelAndView("homepage");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView("loginpage");
        return mav;
    }
    @PostMapping
    public RedirectView loginHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        //session.setAttribute("principal" , request.getAttribute("principal"));
        return new RedirectView("/home", true);
    }


}
