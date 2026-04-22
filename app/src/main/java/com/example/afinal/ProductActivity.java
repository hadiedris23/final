package com.example.afinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {
    private static final int[] INCLUDE_IDS = {
            R.id.productBedroom1, R.id.productBedroom2, R.id.productBedroom3, R.id.productBedroom4, R.id.productBedroom5,
            R.id.productKitchen1, R.id.productKitchen2, R.id.productKitchen3, R.id.productKitchen4, R.id.productKitchen5,
            R.id.productLiving1, R.id.productLiving2, R.id.productLiving3, R.id.productLiving4, R.id.productLiving5,
            R.id.productChair1, R.id.productChair2, R.id.productChair3, R.id.productChair4, R.id.productChair5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bedroom);

        String category = getIntent().getStringExtra("category");
        if (category == null || category.trim().isEmpty()) {
            category = "Furniture";
        }

        populateProducts(category);
    }

    private void populateProducts(String category) {
        for (int i = 0; i < INCLUDE_IDS.length; i++) {
            View include = findViewById(INCLUDE_IDS[i]);
            if (include == null) {
                continue;
            }
            int itemNumber = i + 1;
            TextView titleText = include.findViewById(R.id.titleText);
            TextView nameText = include.findViewById(R.id.product1Name);
            TextView priceText = include.findViewById(R.id.product1Price);
            ImageView image = include.findViewById(R.id.product1Img);
            Button addButton = include.findViewById(R.id.btn1);

            if (titleText != null) {
                titleText.setText(category + " Collection");
            }
            if (nameText != null) {
                nameText.setText(category + " Item " + itemNumber);
            }
            if (priceText != null) {
                int amount = 100 + (itemNumber * 15);
                priceText.setText("$" + amount);
            }
            if (image != null) {
                image.setImageResource(resolveImage(category));
            }
            if (addButton != null && nameText != null && priceText != null) {
                addButton.setOnClickListener(v -> {
                    CartManager.addItem(this, nameText.getText().toString(), priceText.getText().toString());
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private int resolveImage(String category) {
        String normalized = category.toLowerCase();
        if (normalized.contains("kitchen")) {
            return R.drawable.kitchen;
        }
        if (normalized.contains("living")) {
            return R.drawable.livingroom;
        }
        if (normalized.contains("chair") || normalized.contains("sofa")) {
            return R.drawable.chair;
        }
        return R.drawable.beedroom;
    }
}
