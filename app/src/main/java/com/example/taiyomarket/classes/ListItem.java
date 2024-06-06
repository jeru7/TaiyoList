package com.example.taiyomarket.classes;

public class ListItem {
    private int id;
    private String listName;
    private String lastUpdate;

    public ListItem(int id, String listName, String lastUpdate) {
        this.id = id;
        this.listName = listName;
        this.lastUpdate = lastUpdate;
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

}
