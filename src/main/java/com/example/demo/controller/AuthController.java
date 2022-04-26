package com.example.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "Authentication Controller", description = "REST API for handling authentication/authorization")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @PostMapping(value = "/login")
    public ModelAndView homepage(Model model, HttpServletResponse response, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("homepage");
        System.out.println(request.getUserPrincipal().getName());
        return mav;
    }
}
