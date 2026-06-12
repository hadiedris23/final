package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isSignUpMode = false;

    private EditText edtUsername, edtPassword;
    private TextView txtUsernameError, txtPasswordError;
    private Button btnAction;
    private TextView txtToggle;
    private TextView txtTitle;

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
        txtTitle = findViewById(R.id.txtTitle);

        // Admin Secret Mode: Long click title to see all users
        txtTitle.setOnLongClickListener(v -> {
            String allUsers = dbHelper.getAllUsersAsString();
            new AlertDialog.Builder(this)
                    .setTitle("Users Database (Admin Mode)")
                    .setMessage(allUsers)
                    .setPositiveButton("Close", null)
                    .show();
            return true;
        });

        btnAction.setOnClickListener(v -> {
            clearErrors();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString();

            boolean isValid = true;

            if (isSignUpMode) {
                if (!validateUsernameFormat(username)) {
                    txtUsernameError.setText("Username must not contain spaces or special characters");
                    txtUsernameError.setVisibility(View.VISIBLE);
                    isValid = false;
                }
                if (!validatePasswordFormat(password)) {
                    txtPasswordError.setText("Password must be at least 7 characters (Letters & Numbers)");
                    txtPasswordError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (isValid) {
                    if (dbHelper.userExists(username)) {
                        Toast.makeText(this, "Username already exists, try another one.", Toast.LENGTH_LONG).show();
                    } else {
                        if (dbHelper.addUser(username, password)) {
                            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            performLogin(username);
                        }
                    }
                }
            } else {
                if (isValid) {
                    if (dbHelper.userExists(username)) {
                        if (dbHelper.checkUser(username, password)) {
                            performLogin(username);
                        } else {
                            txtPasswordError.setText("Wrong password, try again");
                            txtPasswordError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(this, "User not found. Please sign up.", Toast.LENGTH_LONG).show();
                        toggleMode(true);
                    }
                }
            }
        });

        txtToggle.setOnClickListener(v -> toggleMode(!isSignUpMode));
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
        return Pattern.compile("[a-zA-Z]").matcher(password).find() && Pattern.compile("[0-9]").matcher(password).find();
    }

    private void toggleMode(boolean signUp) {
        isSignUpMode = signUp;
        clearErrors();
        btnAction.setText(isSignUpMode ? "Sign Up" : "Login");
        txtToggle.setText(isSignUpMode ? "Already have an account? Login" : "Don't have an account? Sign Up");
        txtTitle.setText(isSignUpMode ? "Create Account" : "Login");
    }

    private void performLogin(String username) {
        SessionManager.login(this, username);
        Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, home.class));
        finish();
    }
}
