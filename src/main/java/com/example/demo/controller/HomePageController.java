package com.example.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Api(value = "Home page controller", description = "REST API for the home page")
@RestController
@RequestMapping(value = "/")
public class HomePageController {

    @GetMapping()
    public ModelAndView homepage(ModelMap model){

        HttpSession session = (HttpSession) model.getAttribute("session");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        ModelAndView mav = new ModelAndView("homepage");
        HttpServletRequest request = (HttpServletRequest) model.getAttribute("request");
        HttpServletResponse response = (HttpServletResponse) model.getAttribute("response");

        if(authentication != null && authentication.isAuthenticated() && request != null){

            System.out.println(request.getUserPrincipal().getName() + "USER PRINCIPAL NAME");
            //System.out.println("\n SESSION CREATION TIME" + session.getCreationTime());
            System.out.println(" \n \n This does work \n \n ");
            return mav;
        }
        else System.out.println("It doesn't work");
        //System.out.println(request.getAttribute("principal") + "PRINCIPAL");

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView login(Model model){
        ModelAndView mav = new ModelAndView("loginpage");
        return mav;
    }
    @PostMapping
    public ModelAndView loginHandler(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        ModelMap model = new ModelMap();
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("session", session);
        //homepage(response, request, session);
        return new ModelAndView("forward:/", model);
    }


}
