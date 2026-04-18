package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavHelper.bindTopBar(this);

        findViewById(R.id.cardBedroom).setOnClickListener(v -> startActivity(new Intent(this, bedroom.class)));
        findViewById(R.id.cardKitchen).setOnClickListener(v -> startActivity(new Intent(this, kitchen.class)));
        findViewById(R.id.cardLivingRoom).setOnClickListener(v -> startActivity(new Intent(this, livingroom.class)));
        findViewById(R.id.cardChair).setOnClickListener(v -> startActivity(new Intent(this, chairactivity.class)));

        Toast.makeText(this, "Welcome to Luxury Store", Toast.LENGTH_SHORT).show();
    }
}