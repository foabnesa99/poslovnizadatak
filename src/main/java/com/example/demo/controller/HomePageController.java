package com.example.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Api(value = "Home page controller", description = "REST API for the home page")
@RestController
@RequestMapping(value = "/")
public class HomePageController {

    @GetMapping()
    public ModelAndView homepage(Model model){
        ModelAndView mav = new ModelAndView("homepage");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView login(Model model){
        ModelAndView mav = new ModelAndView("loginpage");
        return mav;
    }
}
