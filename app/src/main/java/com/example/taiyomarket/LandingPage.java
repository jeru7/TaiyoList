package com.example.taiyomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taiyomarket.Classes.User;
import com.example.taiyomarket.database.DBHelper;

public class LandingPage extends AppCompatActivity {

    TextView display;

    DBHelper db;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        db = new DBHelper(this);
        display = (TextView) findViewById(R.id.display);

        Intent i = getIntent();
        String userEmail = getIntent().getStringExtra("user_email");

        if (userEmail != null) {
            currentUser = db.getUser(userEmail);
            if (currentUser != null) {
                display.setText(currentUser.getEmail());
                Toast.makeText(this, "dazai pogi", Toast.LENGTH_SHORT).show();
            }
        }

    }
}