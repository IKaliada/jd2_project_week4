package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.servicemodule.UserService;
import com.gmail.rebel249.servicemodule.model.AppUserPrincipal;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final static String USERNAME_EXCEPTION_MESSAGE = "User is not found";

    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDTO userDTO = userService.getUsersByUsername(username);

        if (userDTO == null) {
            throw new UsernameNotFoundException(USERNAME_EXCEPTION_MESSAGE);
        }
        return new AppUserPrincipal(userDTO);
    }
}
