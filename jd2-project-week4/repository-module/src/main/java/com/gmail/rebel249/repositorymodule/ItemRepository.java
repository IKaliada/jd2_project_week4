package com.gmail.rebel249.repositorymodule;


import com.gmail.rebel249.repositorymodule.model.Item;

import java.sql.Connection;
import java.util.List;

public interface ItemRepository extends ConnectionRepository {
    List<Item> getItems(Connection connection);
}
