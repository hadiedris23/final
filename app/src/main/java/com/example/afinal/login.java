package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavHelper.bindTopBar(this);

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLoginNow);
        TextView txtSignUp = findViewById(R.id.txtSignUp);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean hasMinLength = password.length() >= 7;
            boolean hasLetter = password.matches(".*[A-Za-z].*");
            boolean hasDigit = password.matches(".*\\d.*");

            if (!hasMinLength || !hasLetter || !hasDigit) {
                Toast.makeText(this, "الباسورد غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean valid = email.equalsIgnoreCase("student@furniture.com") && password.equals("abc1234");
            if (valid) {
                SessionManager.login(this, email);
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, home.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        txtSignUp.setOnClickListener(v ->
                Toast.makeText(this, "Demo account: student@furniture.com / abc1234", Toast.LENGTH_LONG).show()
        );
    }
}