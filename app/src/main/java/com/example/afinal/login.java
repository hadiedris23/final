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

        // وضع المسؤول السري: اضغط مطولاً على العنوان لرؤية كل المستخدمين
        txtTitle.setOnLongClickListener(v -> {
            String allUsers = dbHelper.getAllUsersAsString();
            new AlertDialog.Builder(this)
                    .setTitle("قاعدة بيانات المستخدمين (وضع المسؤول)")
                    .setMessage(allUsers)
                    .setPositiveButton("إغلاق", null)
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
                    txtUsernameError.setText("اسم المستخدم يجب أن لا يحتوي على مسافات");
                    txtUsernameError.setVisibility(View.VISIBLE);
                    isValid = false;
                }
                if (!validatePasswordFormat(password)) {
                    txtPasswordError.setText("يجب أن تكون كلمة المرور 7 خانات (أحرف وأرقام)");
                    txtPasswordError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (isValid) {
                    if (dbHelper.userExists(username)) {
                        Toast.makeText(this, "هذا الاسم موجود بالفعل، جرب اسماً آخر.", Toast.LENGTH_LONG).show();
                    } else {
                        if (dbHelper.addUser(username, password)) {
                            Toast.makeText(this, "تم إنشاء الحساب محلياً!", Toast.LENGTH_SHORT).show();
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
                            txtPasswordError.setText("كلمة المرور خاطئة، حاول مرة أخرى");
                            txtPasswordError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(this, "هذا المستخدم غير موجود. يرجى إنشاء حساب.", Toast.LENGTH_LONG).show();
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
        btnAction.setText(isSignUpMode ? "إنشاء حساب" : "تسجيل دخول");
        txtToggle.setText(isSignUpMode ? "لديك حساب بالفعل؟ سجل دخولك" : "ليس لديك حساب؟ سجل الآن");
        txtTitle.setText(isSignUpMode ? "إنشاء حساب" : "تسجيل دخول");
    }

    private void performLogin(String username) {
        SessionManager.login(this, username);
        Toast.makeText(this, "مرحباً بك، " + username, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, home.class));
        finish();
    }
}