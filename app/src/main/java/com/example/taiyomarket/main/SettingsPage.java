package com.example.taiyomarket.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taiyomarket.R;
import com.example.taiyomarket.classes.User;
import com.example.taiyomarket.database.DBHelper;

public class SettingsPage extends AppCompatActivity {
    TextView username, email;
    long userId;
    User currentUser;
    DBHelper db;
    ImageView back, editUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", -1);
        db = new DBHelper(this);
        currentUser = db.getUserById(userId);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        back = findViewById(R.id.back_btn);
        editUsername = findViewById(R.id.edit_username);

        updateElements();
        attachButtonEvents();
    }

    public void attachButtonEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditUsernameDialog();
            }
        });
    }
    private void showEditUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Username");

        EditText input = new EditText(this);
        input.setText(currentUser.getUsername());
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString().trim();
                if (!newUsername.isEmpty()) {
                    boolean success = db.setUsernameById(userId, newUsername);

                    if (success) {
                        currentUser.setUsername(newUsername);
                        updateElements();
                    } else {
                        Toast.makeText(SettingsPage.this, "Failed to update the username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateElements() {
        String usernameValue = db.getUsernameById(userId);
        if(usernameValue == null) {
            username.setText("Add username");
        } else {
            username.setText(usernameValue);
        }

        email.setText(currentUser.getEmail());
    }
}