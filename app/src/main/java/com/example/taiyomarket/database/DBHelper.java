package com.example.taiyomarket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taiyomarket.Classes.User;

public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "TaiyoMarket.db";
    private static final int DB_VERSION = 1;
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

//    returns -1 if there's a problem in adding the user to users table.
    public long addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("email", email);
        content.put("password", password);

        long userId = db.insert("users", null, content);

        db.close();

        return userId;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query("users", null, "email = ?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            user = new User(id, userEmail, password);
            cursor.close();
        }

        db.close();
        return user;
    }

    public Boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String querySelection = "email = ? AND password = ?";
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, quantity INTEGER, category_id INTEGER, FOREIGN KEY(category_id) REFERENCES categories(id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS categories (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, product_id INTEGER, quantity INTEGER, FOREIGN KEY(user_id) REFERENCES users(id), FOREIGN KEY(product_id) REFERENCES products(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS orders");

        onCreate(db);
    }
}
