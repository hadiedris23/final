package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        checkoutButton.setOnClickListener(v -> startActivity(new Intent(this, pay.class)));
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
            empty.setTextColor(getColor(android.R.color.white));
            empty.setTextSize(16f);
            cartItemsLayout.addView(empty);
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            final int index = i;
            String item = items.get(i);
            Button itemRow = new Button(this);
            itemRow.setText("Remove: " + item);
            itemRow.setAllCaps(false);
            itemRow.setOnClickListener(v -> {
                CartManager.removeAt(this, index);
                renderCart();
            });
            cartItemsLayout.addView(itemRow);
        }
    }
}
