package com.example.taiyomarket.classes;

public class Item {
    private int id;
    private String itemName;
    private int quantity;
    private String dateCreated;
    private boolean status;

    public Item(int id, String itemName, int quantity, String dateCreated, boolean status) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isStatus() {
        return status;
    }
}
