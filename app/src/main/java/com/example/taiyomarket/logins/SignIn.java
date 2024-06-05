package com.example.taiyomarket.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taiyomarket.main.LandingPage;
import com.example.taiyomarket.R;
import com.example.taiyomarket.database.DBHelper;

public class SignIn extends AppCompatActivity {
    ImageView back, passToggler;

    EditText email, password;

    TextView forgotPass, loginGuide;

    Button register, signIn;

    String emailFinal, passwordFinal;
    Boolean isSignInValid = false;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        db = new DBHelper(this);

        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);
        back = (ImageView) findViewById(R.id.back_btn);
        passToggler = (ImageView) findViewById(R.id.password_toggler);
        register = (Button) findViewById(R.id.register_btn);
        signIn = (Button) findViewById(R.id.sign_in_btn);
        forgotPass = (TextView) findViewById(R.id.forgot_pass);

        attachTextListener();
        attachButtonEvents();
    }

    public void signInHandler(String email, String password) {
        emailFinal = email;
        passwordFinal = password;

        if(!emailFinal.isEmpty() && !passwordFinal.isEmpty()) {
            isSignInValid = true;
        } else {
            isSignInValid = false;
        }

        buttonEnabler();
    }

    public void loginHandler(String email, String password) {
        loginGuide = (TextView) findViewById(R.id.login_guide);

        if(db.checkUser(email, password)) {
            Intent i = new Intent(SignIn.this, LandingPage.class);
            loginGuide.setText("");
            i.putExtra("email", email);
            startActivity(i);
        } else {
            loginGuide.setText("Incorrect email or password");
        }
    }

    public void attachTextListener() {

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signInHandler(s.toString(), password.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signInHandler(email.getText().toString(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void buttonEnabler() {
        if(isSignInValid) {
            signIn.setEnabled(true);
            signIn.setBackgroundResource(R.drawable.enabled_button);
        } else {
            signIn.setEnabled(false);
            signIn.setBackgroundResource(R.drawable.disabled_button);
        }
    }

    public void attachButtonEvents() {
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
                loginHandler(emailFinal, passwordFinal);
            }
        });
    }
}