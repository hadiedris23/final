package com.example.afinal;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
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
        Button payButton = findViewById(R.id.btnPay);

        int itemCount = CartManager.getItems(this).size();
        int amount = itemCount == 0 ? 0 : itemCount * 115;
        total.setText("₪" + amount);

        payButton.setOnClickListener(v -> {
            EditText paymentInput = new EditText(this);
            paymentInput.setHint("Enter payment details");
            paymentInput.setInputType(InputType.TYPE_CLASS_TEXT);

            new AlertDialog.Builder(this)
                    .setTitle("Complete Payment")
                    .setMessage("Enter any payment info you want:")
                    .setView(paymentInput)
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        String paymentInfo = paymentInput.getText().toString().trim();
                        if (paymentInfo.isEmpty()) {
                            Toast.makeText(this, "Please enter payment details", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CartManager.clear(this);
                        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}