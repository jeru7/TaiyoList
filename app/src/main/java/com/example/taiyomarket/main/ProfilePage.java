package com.example.taiyomarket.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

public class ProfilePage extends AppCompatActivity {

    LinearLayout settingsBtn, supportBtn, logoutBtn, listPage;
    int userId;
    User currentUser;
    DBHelper db;
    TextView userNameEl, userEmailEl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        settingsBtn = findViewById(R.id.settings);
        supportBtn = findViewById(R.id.support);
        logoutBtn = findViewById(R.id.logout);
        listPage = findViewById(R.id.list_layout);

        userNameEl = findViewById(R.id.username);
        userEmailEl = findViewById(R.id.user_email);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        db = new DBHelper(this);
        currentUser = db.getUserById(userId);
        attachElements();
        attachButtonEvents();
    }

    public void attachElements() {
        String username = currentUser.getUsername();
        if(username != null) {
            userNameEl.setText(username);
        } else {
            userNameEl.setText("Add Username");
        }

        String email = currentUser.getEmail();
        userEmailEl.setText(email);
    }

    public void attachButtonEvents() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, SettingsPage.class);
                startActivity(i);
            }
        });

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this, "Customer support is not available", Toast.LENGTH_SHORT).show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        listPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ProfilePage.this, WelcomePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}