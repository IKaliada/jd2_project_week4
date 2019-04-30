package com.gmail.rebel249.springbootmodule.controller;

import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("private/items")
    public String showItems(Model model) {
        List<ItemDTO> items = itemService.getItems();
        model.addAttribute("items", items);
        return "items";
    }
}
