package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isSignUpMode = false;

    private EditText edtUsername, edtPassword;
    private TextView txtUsernameError, txtPasswordError;
    private Button btnAction;
    private TextView txtToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        NavHelper.bindTopBar(this);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtUsernameError = findViewById(R.id.txtUsernameError);
        txtPasswordError = findViewById(R.id.txtPasswordError);
        btnAction = findViewById(R.id.btnLoginNow);
        txtToggle = findViewById(R.id.txtSignUp);

        btnAction.setOnClickListener(v -> {
            clearErrors();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString();

            boolean isValid = true;

            if (isSignUpMode) {
                if (!validateUsernameFormat(username)) {
                    txtUsernameError.setText("the username not include the spaces or anything, just the letters and numbers");
                    txtUsernameError.setVisibility(View.VISIBLE);
                    isValid = false;
                }
                if (!validatePasswordFormat(password)) {
                    txtPasswordError.setText("you have a wrong password, you have to include certain letters");
                    txtPasswordError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (isValid) {
                    if (dbHelper.userExists(username)) {
                        Toast.makeText(this, "This username exists, change it. If this username is for you, do a login, you don't have to sign up.", Toast.LENGTH_LONG).show();
                    } else {
                        if (dbHelper.addUser(username, password)) {
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            performLogin(username);
                        } else {
                            Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                if (username.isEmpty()) {
                    txtUsernameError.setText("Please enter username");
                    txtUsernameError.setVisibility(View.VISIBLE);
                    isValid = false;
                }
                
                if (isValid) {
                    if (dbHelper.userExists(username)) {
                        if (dbHelper.checkUser(username, password)) {
                            performLogin(username);
                        } else {
                            txtPasswordError.setText("you have to put a right password or the password is wrong");
                            txtPasswordError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(this, "User does not exist. Please sign up.", Toast.LENGTH_LONG).show();
                        toggleMode(true);
                    }
                }
            }
        });

        txtToggle.setOnClickListener(v -> {
            toggleMode(!isSignUpMode);
        });
    }

    private void clearErrors() {
        txtUsernameError.setVisibility(View.GONE);
        txtPasswordError.setVisibility(View.GONE);
    }

    private boolean validateUsernameFormat(String username) {
        return !username.isEmpty() && Pattern.matches("^[a-zA-Z0-9]+$", username);
    }

    private boolean validatePasswordFormat(String password) {
        if (password.length() < 7) return false;
        if (password.contains(" ")) return false;
        boolean hasLetter = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        return hasLetter && hasDigit;
    }

    private void toggleMode(boolean signUp) {
        isSignUpMode = signUp;
        clearErrors();
        if (isSignUpMode) {
            btnAction.setText("Sign Up");
            txtToggle.setText("Already have an account? Login");
            ((TextView)findViewById(R.id.txtTitle)).setText("Sign Up");
        } else {
            btnAction.setText("Login");
            txtToggle.setText("Don't have an account? Sign Up");
            ((TextView)findViewById(R.id.txtTitle)).setText("Login");
        }
    }

    private void performLogin(String username) {
        SessionManager.login(this, username);
        Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, home.class));
        finish();
    }
}