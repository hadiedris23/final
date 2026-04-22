package com.example.afinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class cart extends AppCompatActivity {
    private LinearLayout cartItemsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        NavHelper.bindTopBar(this);
        cartItemsLayout = findViewById(R.id.cartItemsLayout);
        Button checkoutButton = findViewById(R.id.btnCheckout);

        checkoutButton.setOnClickListener(v -> {
            List<String> items = CartManager.getItems(this);
            if (items.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, pay.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderCart();
    }

    private void renderCart() {
        cartItemsLayout.removeAllViews();
        List<String> items = CartManager.getItems(this);
        
        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No items in cart yet.");
            empty.setTextColor(Color.WHITE);
            empty.setTextSize(18f);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, 50, 0, 0);
            cartItemsLayout.addView(empty);
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            final int index = i;
            String item = items.get(i);
            
            LinearLayout itemRow = new LinearLayout(this);
            itemRow.setOrientation(LinearLayout.HORIZONTAL);
            itemRow.setBackgroundColor(Color.parseColor("#D7CCC8"));
            itemRow.setPadding(20, 20, 20, 20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 10);
            itemRow.setLayoutParams(params);

            TextView nameText = new TextView(this);
            nameText.setText(item);
            nameText.setTextColor(Color.parseColor("#4E342E"));
            nameText.setTextSize(16f);
            nameText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            itemRow.addView(nameText);

            Button removeBtn = new Button(this);
            removeBtn.setText("X");
            removeBtn.setBackgroundColor(Color.RED);
            removeBtn.setTextColor(Color.WHITE);
            removeBtn.setOnClickListener(v -> {
                CartManager.removeAt(this, index);
                renderCart();
            });
            itemRow.addView(removeBtn);

            cartItemsLayout.addView(itemRow);
        }
    }
}