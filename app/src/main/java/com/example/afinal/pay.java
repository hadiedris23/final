package com.example.afinal;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class pay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        NavHelper.bindTopBar(this);

        TextView total = findViewById(R.id.txtTotalAmount);
        
        // Use a default amount if CartManager isn't available or empty
        // In a real app, this should come from the cart
        total.setText("$370");

        findViewById(R.id.btnGooglePay).setOnClickListener(v -> showGooglePayDialog());
        findViewById(R.id.btnPayPal).setOnClickListener(v -> showPayPalDialog());
        findViewById(R.id.btnVisa).setOnClickListener(v -> showVisaDialog());
    }

    private void showGooglePayDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Google Gmail");
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        new AlertDialog.Builder(this)
                .setTitle("Google Pay")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (emailInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPayPalDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("PayPal Email");
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        new AlertDialog.Builder(this)
                .setTitle("PayPal")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (emailInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showVisaDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Cardholder Name");
        layout.addView(nameInput);

        final EditText numberInput = new EditText(this);
        numberInput.setHint("Card Number");
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(numberInput);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        final EditText dateInput = new EditText(this);
        dateInput.setHint("MM/YY");
        dateInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(dateInput);

        final EditText cvvInput = new EditText(this);
        cvvInput.setHint("CVV");
        cvvInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvvInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(cvvInput);

        layout.addView(row);

        new AlertDialog.Builder(this)
                .setTitle("Visa / Credit Card")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (nameInput.getText().toString().isEmpty() || numberInput.getText().toString().isEmpty() ||
                        dateInput.getText().toString().isEmpty() || cvvInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void processPayment() {
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
        // Clear cart if CartManager exists
        try {
            CartManager.clear(this);
        } catch (Exception ignored) {}
        finish();
    }
}