package com.gmail.rebel249.servicemodule;

import com.gmail.rebel249.repositorymodule.model.User;
import com.gmail.rebel249.servicemodule.model.UserDTO;

public interface UserConverter {

    User fromUserDTO(UserDTO userDTO);

    UserDTO toUserDTO(User user);
}
