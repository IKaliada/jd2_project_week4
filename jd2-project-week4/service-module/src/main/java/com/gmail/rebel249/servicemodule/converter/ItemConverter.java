package com.gmail.rebel249.servicemodule.converter;


import com.gmail.rebel249.repositorymodule.model.Item;
import com.gmail.rebel249.servicemodule.model.ItemDTO;

public interface ItemConverter {

    Item fromItemDTO(ItemDTO itemDTO);

    ItemDTO toItemDTO(Item item);
}
