package com.example.taiyomarket.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private long id;
    private String itemName;
    private int quantity;
    private String dateCreated;
    private boolean checked = false;

    public Item(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.dateCreated = getCurrentDate();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getId() {
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

    public boolean isChecked() {
        return checked;
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}
