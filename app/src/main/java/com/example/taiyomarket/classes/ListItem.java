package com.example.taiyomarket.classes;

public class ListItem {
    private int id;
    private String listName;
    private String lastUpdate;
    private String dateCreated;

    public ListItem(int id, String listName, String dateCreated ,String lastUpdate) {
        this.id = id;
        this.listName = listName;
        this.dateCreated = dateCreated;
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

    public String getDateCreated() {
        return dateCreated;
    }
}
