package com.example.taiyomarket.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taiyomarket.LandingPage;
import com.example.taiyomarket.R;
import com.example.taiyomarket.database.DBHelper;

import org.w3c.dom.Text;

public class SignIn extends AppCompatActivity {
    ImageView back, passToggler;

    EditText email, password;

    TextView forgotPass, loginGuide;

    Button register, signIn;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        db = new DBHelper(this);

//        input fields
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);


//        buttons - (button like)
        back = (ImageView) findViewById(R.id.back_btn);
        passToggler = (ImageView) findViewById(R.id.password_toggler);
        register = (Button) findViewById(R.id.register_btn);
        signIn = (Button) findViewById(R.id.sign_in_btn);
        forgotPass = (TextView) findViewById(R.id.forgot_pass);

        attachButtonEvents(back, passToggler, register, signIn, forgotPass, password, email, loginGuide);
    }

    public void loginHandler(String email, String password) {
        loginGuide = (TextView) findViewById(R.id.login_guide);

        if(db.checkUser(email, password)) {
            Intent i = new Intent(SignIn.this, LandingPage.class);
            loginGuide.setText("");
            i.putExtra("user_email", email);
            startActivity(i);
        } else {
            loginGuide.setText("Incorrect email or password");
        }
    }

    public void attachButtonEvents(ImageView back, ImageView passToggler, Button register, Button signIn, TextView forgotPass, EditText password, EditText email, TextView loginGuide) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        passToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = password.getSelectionStart();

                if(password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passToggler.setImageResource(R.drawable.eye_closed);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passToggler.setImageResource(R.drawable.eye_icon);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }

                password.setSelection(currentPos);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, Register.class);
                startActivity(i);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                loginHandler(emailInput, passwordInput);
            }
        });
    }
}