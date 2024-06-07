package com.example.taiyomarket.classes;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private int id;
    private String listName;
    private String lastUpdate;
    private String dateCreated;
    private List<Item> itemList;


    public ListItem(int id, String listName, String dateCreated ,String lastUpdate) {
        this.id = id;
        this.listName = listName;
        this.dateCreated = dateCreated;
        this.lastUpdate = lastUpdate;
        this.itemList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void addItem(Item item) {
        itemList.add(item);
    }
}
