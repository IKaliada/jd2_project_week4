package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.repositorymodule.model.User;
import com.gmail.rebel249.servicemodule.UserConverter;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {
    private final static Logger logger = LoggerFactory.getLogger(UserConverterImpl.class);

    @Override
    public User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    @Override
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setPassword(user.getPassword());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
