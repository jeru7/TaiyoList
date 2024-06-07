package com.example.taiyomarket.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private int id;
    private String itemName;
    private int quantity;
    private String dateCreated;
    private boolean status = false;

    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.dateCreated = getCurrentDate();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
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
