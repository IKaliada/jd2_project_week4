package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.servicemodule.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
}
