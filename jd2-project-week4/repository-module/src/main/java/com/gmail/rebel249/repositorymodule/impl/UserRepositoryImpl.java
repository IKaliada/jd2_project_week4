package com.gmail.rebel249.repositorymodule.impl;

import com.gmail.rebel249.repositorymodule.UserRepository;
import com.gmail.rebel249.repositorymodule.exception.IllegalDatabaseStateException;
import com.gmail.rebel249.repositorymodule.exception.IllegalFormatStatementRepositoryException;
import com.gmail.rebel249.repositorymodule.model.Role;
import com.gmail.rebel249.repositorymodule.model.User;
import com.gmail.rebel249.repositorymodule.property.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl extends ConnectionRepositoryImpl implements UserRepository {

    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final String DATABASE_STATE_MESSAGE = "Database exception during getting user";
    private static final String STATEMENT_REPOSITORY_MESSAGE = "Cannot execute SQL query ";

    @Autowired
    public UserRepositoryImpl(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    public User getUsersByUsername(Connection connection, String username) {
        String userQuery = "SELECT u.id, u.username, u.password, u.role_id, r.name FROM user u JOIN role r " +
                "ON u.role_id = r.id WHERE username = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(userQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return getUser(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalFormatStatementRepositoryException(STATEMENT_REPOSITORY_MESSAGE + userQuery);
        }
        return null;
    }

    @Override
    public List<User> getUsers(Connection connection) {
        List<User> users = new ArrayList<>();
        String userQuery = "SELECT u.id, u.username, u.password, u.role_id, r.name FROM user u JOIN role r " +
                "ON u.role_id = r.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(userQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalFormatStatementRepositoryException(STATEMENT_REPOSITORY_MESSAGE + userQuery);
        }
        return users;
    }

    private User getUser(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            Role role = new Role();
            role.setId(resultSet.getLong("role_id"));
            role.setName(resultSet.getString("name"));
            user.setRole(role);
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new IllegalDatabaseStateException(DATABASE_STATE_MESSAGE);
        }
    }

    @Override
    public void add(User user, Connection connection) {
        String userQuery = "INSERT INTO user (username, password, role_id) VALUES (?, ?, (SELECT r.id FROM role r WHERE r.name = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(userQuery)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalFormatStatementRepositoryException(STATEMENT_REPOSITORY_MESSAGE + userQuery);
        }
    }
}
