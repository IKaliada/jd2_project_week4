package com.gmail.rebel249.servicemodule;

import com.gmail.rebel249.servicemodule.model.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUsersByUsername(String username);

    List<UserDTO> getUsers();

    void add(UserDTO userDTO);
}
