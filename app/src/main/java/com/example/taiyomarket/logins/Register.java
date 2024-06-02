package com.example.taiyomarket.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.*;

import com.example.taiyomarket.LandingPage;
import com.example.taiyomarket.R;
import com.example.taiyomarket.database.DBHelper;


public class Register extends AppCompatActivity {

//    variables attached to elements in Register activity
    Button register;
    ImageView back;
    TextView sign_in;
    EditText email, password, passwordConfirmation;

//    global variables
    String confirmedEmail, confirmedPassword;

    Boolean isEmail = false;
    Boolean isPassword = false;
    Boolean isRegistrationValid = false;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);

        back = (ImageView) findViewById(R.id.back_btn);
        register = (Button) findViewById(R.id.register_btn);
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);
        passwordConfirmation = (EditText) findViewById(R.id.confirm_password_field);

        attachTextChangeListeners();
        buttonEnabler(register);
        attachButtonEvents(back, register);
    }

//    registers the user and INSERT its information to the users table in TaiyoMarket.db

    public void registerUser() {
        if(isRegistrationValid) {
            long userId = db.addUser(confirmedEmail, confirmedPassword);

            if(userId != -1) {
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Register.this, SignIn.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    toggles the register button enabler
    public void buttonEnabler (Button register) {
        if(isRegistrationValid) {
            register.setEnabled(true);
            register.setBackgroundResource(R.drawable.enabled_button);
        } else {
            register.setEnabled(false);
            register.setBackgroundResource(R.drawable.disabled_button);
        }
    }

//    runs every time there's a change on the input field of the specified field.
    public void attachTextChangeListeners() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailHandler(email);
                loginHandler();
                buttonEnabler(register);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordHandler(password);
                loginHandler();
                buttonEnabler(register);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        passwordConfirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasswordHandler(passwordConfirmation, password);
                loginHandler();
                buttonEnabler(register);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

//    handles email value (pwede maglagay ng regex for emails)
    public void emailHandler(EditText email) {
        String emailValue = email.getText().toString();

        if(!emailValue.isEmpty()) {
            confirmedEmail = emailValue;
            isEmail = true;
        } else {
            isEmail = false;
        }
    }

//    handles password value
    public void passwordHandler(EditText password) {
        String passwordValue = password.getText().toString();

        if (!passwordValue.isEmpty()) {
            passwordChecker(passwordValue);
            isPassword = true;
        } else {
            isPassword = false;
        }
    }

//    handles confirmation password value
    public void confirmPasswordHandler(EditText passwordConfirmation, EditText password) {
        String passwordValue = password.getText().toString();
        String passwordConfirmationValue = passwordConfirmation.getText().toString();
        isPassword = confirmationHandler(passwordValue, passwordConfirmationValue) && !passwordConfirmationValue.isEmpty();
    }

//  toggles the flag 'isRestrationValid' based on the flag isPassword and isEmail value
    public void loginHandler() {
        if(isPassword && isEmail) {
            isRegistrationValid = true;
        } else {
            isRegistrationValid = false;
        }
    }

//  handles confirm password value
    public Boolean confirmationHandler(String password, String confirmationPassword) {
        TextView confirmationPasswordGuide = (TextView) findViewById(R.id.confirm_password_guide);

        if(password.equals(confirmationPassword)) {
            confirmationPasswordGuide.setText("");
            return true;
        }  else {
            confirmationPasswordGuide.setText("Password doesn't match");
        }

        return false;
    }

//    validate the password format based on the given regex pattern
    public void passwordChecker(String password) {
        TextView passwordGuide = (TextView) findViewById(R.id.password_guide);

        Boolean isValid = false;

//        pattern: at least 8 characters, must have at least 1 special character AND 1 capital letter
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
        Pattern pattern1 = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern1.matcher(password);


        if (password.length() < 8) {
            passwordGuide.setText("Password must have at least 8 characters");
        } else if (!matcher.find()) {
            if (!Pattern.compile("(?=.*[A-Z])").matcher(password).find()) {
                passwordGuide.setText("Password needs at least 1 capital letter");
            } else if (!Pattern.compile("(?=.*[!@#$%^&*(),.?\":{}|<>])").matcher(password).find()) {
                passwordGuide.setText("Password must have at least 1 special character");
            }
        } else {
            passwordGuide.setText("");
        }
    }

    public void attachButtonEvents(ImageView back, Button register) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
}