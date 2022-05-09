package com.example.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Api(value = "Home page controller", description = "REST API for the home page")
@RestController
@RequestMapping(value = "/")
@Slf4j
public class HomePageController {

    @GetMapping("/home")
    public ModelAndView homepage(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        ModelAndView mav = new ModelAndView("homepage");
        return mav;
    }

    @GetMapping("/forbidden")
    public ModelAndView forbiddenPage(){
        return new ModelAndView("forbidden");
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView("loginpage");
        return mav;
    }
    @PostMapping
    public RedirectView loginHandler() {
        return new RedirectView("/home", true);
    }
    @GetMapping
    public RedirectView homeRedirect(){
        return new RedirectView("/home", true);
    }


}
