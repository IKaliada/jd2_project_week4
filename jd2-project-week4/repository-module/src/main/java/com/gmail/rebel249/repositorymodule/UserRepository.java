package com.gmail.rebel249.repositorymodule;

import com.gmail.rebel249.repositorymodule.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends ConnectionRepository {

    User getUsersByUsername(Connection connection, String username);

    List<User> getUsers(Connection connection);

    void add(User user, Connection connection);
}
