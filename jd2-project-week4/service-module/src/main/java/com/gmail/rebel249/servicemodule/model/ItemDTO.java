package com.gmail.rebel249.servicemodule.model;


import com.gmail.rebel249.repositorymodule.model.ItemStatusEnum;

public class ItemDTO {

    private Long id;
    private String name;
    private ItemStatusEnum itemStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStatusEnum getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatusEnum itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemStatus=" + itemStatus +
                '}';
    }
}
