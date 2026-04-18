package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.cardview.widget.CardView; // تأكد من استيراد CardView

public class store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bedroom);

        // تعيين Padding للـ system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ربط كل CardView بالـ XML
        CardView cardBedroom = findViewById(R.id.cardBedroom);
        CardView cardKitchen = findViewById(R.id.cardKitchen);
        CardView cardLivingRoom = findViewById(R.id.cardLivingRoom);
        CardView cardChair = findViewById(R.id.cardChair);

        // الضغط على Bedroom
        cardBedroom.setOnClickListener(v -> {
            Intent intent = new Intent(store.this, ProductActivity.class);
            intent.putExtra("title", "Bedroom");
            intent.putExtra("desc", "High-quality beds, wardrobes, and bedroom furniture.");
            intent.putExtra("imageRes", R.drawable.beedroom);
            startActivity(intent);
        });

        // الضغط على Kitchen
        cardKitchen.setOnClickListener(v -> {
            Intent intent = new Intent(store.this, ProductActivity.class);
            intent.putExtra("title", "Kitchen");
            intent.putExtra("desc", "Stylish kitchen furniture and dining tables.");
            intent.putExtra("imageRes", R.drawable.kitchen);
            startActivity(intent);
        });

        // الضغط على Living Room
        cardLivingRoom.setOnClickListener(v -> {
            Intent intent = new Intent(store.this, ProductActivity.class);
            intent.putExtra("title", "Living Room");
            intent.putExtra("desc", "Elegant sofas, chairs, and tables for your living room.");
            intent.putExtra("imageRes", R.drawable.livingroom);
            startActivity(intent);
        });

        // الضغط على Chair / Sofa
        cardChair.setOnClickListener(v -> {
            Intent intent = new Intent(store.this, ProductActivity.class);
            intent.putExtra("title", "Chair / Sofa");
            intent.putExtra("desc", "Comfortable chairs and sofas with modern design.");
            intent.putExtra("imageRes", R.drawable.chair);
            startActivity(intent);
        });
    }
}