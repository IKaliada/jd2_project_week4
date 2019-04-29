package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.repositorymodule.UserRepository;
import com.gmail.rebel249.repositorymodule.model.Role;
import com.gmail.rebel249.repositorymodule.model.User;
import com.gmail.rebel249.servicemodule.UserConverter;
import com.gmail.rebel249.servicemodule.UserService;
import com.gmail.rebel249.servicemodule.exception.ConnectionServiceStateException;
import com.gmail.rebel249.servicemodule.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String CONNECTION_SERVICE_MESSAGE = "Cannot create connection";

    private UserRepository userRepository;
    private UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO getUsersByUsername(String username) {
        try (Connection connection = userRepository.getConnection()) {
            UserDTO userDTO = null;
            try {
                connection.setAutoCommit(false);
                User user = userRepository.getUsersByUsername(connection, username);
                userDTO = userConverter.toUserDTO(user);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
            }
            return userDTO;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
        }
    }

    @Override
    public List<UserDTO> getUsers() {
        try (Connection connection = userRepository.getConnection()) {
            List<UserDTO> userDTOS;
            try {
                connection.setAutoCommit(false);
                List<User> users = userRepository.getUsers(connection);
                userDTOS = users.stream().map(userConverter::toUserDTO).collect(Collectors.toList());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
            }
            return userDTOS;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
        }
    }

    @Override
    public void add(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Role role = new Role();
                role.setName("ROLE_CUSTOMER");
                userDTO.setRole(role);
                User user = userConverter.fromUserDTO(userDTO);
                user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
                logger.info(user.toString());
                userRepository.add(user, connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage());
                throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
        }
    }
}
