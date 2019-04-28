package com.gmail.rebel249.servicemodule.impl;

import com.gmail.rebel249.repositorymodule.ItemRepository;
import com.gmail.rebel249.repositorymodule.model.Item;
import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.converter.ItemConverter;
import com.gmail.rebel249.servicemodule.exception.ConnectionServiceStateException;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private static final String CONNECTION_SERVICE_MESSAGE = "Cannot create connection";
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemConverter itemConverter) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    public List<ItemDTO> getItems() {
        try (Connection connection = itemRepository.getConnection()) {
            List<ItemDTO> itemDTOList;
            try {
                connection.setAutoCommit(false);
                List<Item> items = itemRepository.getItems(connection);
                itemDTOList = items.stream().map(itemConverter::toItemDTO).collect(Collectors.toList());
                connection.commit();
                return itemDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionServiceStateException(CONNECTION_SERVICE_MESSAGE);
        }
    }
}
