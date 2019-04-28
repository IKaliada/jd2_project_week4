package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.servicemodule.UserService;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/private/users")
    public String getUsers(Model model) {
        List<UserDTO> users = userService.getUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/login")
    public String loginUser(UserDTO userDTO) {
        return "login";
    }

    @GetMapping("/403")
    public String get403() {
        return "403";
    }

    @PostMapping("/login")
    public String loginUserToSite(@Valid UserDTO userDTO, Model model, BindingResult bindingResult) {
        logger.info(userDTO.toString());
        if (bindingResult.hasErrors()) {
            return "login";
        }
        model.addAttribute("userDTO", userDTO);
        return "redirect:/private/users";
    }
}
