package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.servicemodule.UserService;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersAPIController {

    private UserService userService;

    @Autowired
    public UsersAPIController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/private/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/private/users")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        return new ResponseEntity<>(userDTO.toString(), HttpStatus.OK);
    }
}
