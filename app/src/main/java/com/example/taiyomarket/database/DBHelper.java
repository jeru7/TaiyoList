package com.example.taiyomarket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.classes.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "TAIYOMARKET";
    private static final int DB_VERSION = 2;
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



//    adds the user to the database (Register)
    public long addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("email", email);
        content.put("password", password);

        long userId = db.insert("users", null, content);

        db.close();

        return userId;
    }

//    gets the user based on the email
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query("users", null, "email = ? COLLATE NOCASE", new String[]{email}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("user_id"));
            String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            user = new User(id, userEmail, password);
            cursor.close();
        }

        if(cursor != null) {
            cursor.close();
        }

        db.close();
        return user;
    }

//    checks if the user exists within the database (Sign in)
    public Boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"user_id"};
        String querySelection = "email = ? COLLATE NOCASE AND password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = null;

        try {
            cursor = db.query("users", columns, querySelection, selectionArgs, null, null, null);
            if(cursor != null && cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
            db.close();
        }

    }

//    adds a list to the user's grocery_list
    public long addList(int userId, String listName, String dateCreated, String lastUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("list_name", listName);
        values.put("date_created", dateCreated);
        values.put("lastUpdate", lastUpdate);

        long listId = db.insert("grocery_list", null, values);

        db.close();

        return listId;
    }

//    fetch the list of the user based on their email
    public List<ListItem> getLists(String email) {
        List<ListItem> lists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM grocery_list WHERE user_id = (SELECT user_id FROM users WHERE email = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if(cursor.moveToFirst()) {
            do {
                int listId = cursor.getInt(cursor.getColumnIndex("list_id"));
                String listName = cursor.getString(cursor.getColumnIndex("list_name"));
                String dateCreated = cursor.getString(cursor.getColumnIndex("date_created"));
                String lastUpdate = cursor.getString(cursor.getColumnIndex("last_update"));
                lists.add(new ListItem(listId, listName, dateCreated,lastUpdate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lists;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (user_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, user_name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS grocery_list (list_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INT, list_name TEXT, date_created DATE, last_update DATE, FOREIGN KEY(user_id) REFERENCES users(user_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS item (item_id INTEGER PRIMARY KEY AUTOINCREMENT, list_id INT, item_name TEXT, quantity INT, date_created DATE, status BOOLEAN)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS grocery_list");
        db.execSQL("DROP TABLE IF EXISTS item");

        onCreate(db);
    }
}
