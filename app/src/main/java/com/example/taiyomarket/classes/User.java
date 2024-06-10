package com.example.taiyomarket.classes;
import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String password;
    private String username;
    private ArrayList<Integer> lists;

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lists = new ArrayList<>();
    };

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addList(int list_id) {
        lists.add(list_id);
    }



}
