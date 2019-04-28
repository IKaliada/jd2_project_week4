package com.gmail.rebel249.repositorymodule.impl;

import com.gmail.rebel249.repositorymodule.ItemRepository;
import com.gmail.rebel249.repositorymodule.exception.IllegalDatabaseStateException;
import com.gmail.rebel249.repositorymodule.exception.IllegalFormatStatementRepositoryException;
import com.gmail.rebel249.repositorymodule.model.Item;
import com.gmail.rebel249.repositorymodule.model.ItemStatus;
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
public class ItemRepositoryImpl extends ConnectionRepositoryImpl implements ItemRepository {

    private final static Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);
    private static final String DATABASE_STATE_MESSAGE = "Database exception during getting user";
    private static final String STATEMENT_REPOSITORY_MESSAGE = "Cannot execute SQL query ";

    @Autowired
    public ItemRepositoryImpl(DatabaseProperties databaseProperties) {
        super(databaseProperties);
    }

    @Override
    public List<Item> getItems(Connection connection) {
        List<Item> items = new ArrayList<>();
        String itemQuery = "SELECT * FROM item";
        try (PreparedStatement preparedStatement = connection.prepareStatement(itemQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = getItem(resultSet);
                items.add(item);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new IllegalFormatStatementRepositoryException(STATEMENT_REPOSITORY_MESSAGE + itemQuery);
        }
        return items;
    }

    private Item getItem(ResultSet resultSet) {
        try {
            Item item = new Item();
            item.setId(resultSet.getLong("id"));
            item.setName(resultSet.getString("name"));
            item.setItemStatus(ItemStatus.valueOf(resultSet.getString("item_status")));
            return item;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalDatabaseStateException(DATABASE_STATE_MESSAGE);
        }
    }
}
