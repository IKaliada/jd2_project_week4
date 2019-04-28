package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemsAPIController {

    private ItemService itemService;

    @Autowired
    public ItemsAPIController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/private/items")
    public ResponseEntity<List<ItemDTO>> getUsers() {
        List<ItemDTO> items = itemService.getItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

