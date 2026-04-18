package com.example.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        NavHelper.bindTopBar(this);

        CardView cardShopLocation = findViewById(R.id.cardShopLocation);
        Button btnOpenMap = findViewById(R.id.btnOpenMap);
        cardShopLocation.setOnClickListener(v -> openShopLocation());
        btnOpenMap.setOnClickListener(v -> openShopLocation());
    }

    private void openShopLocation() {
        double lat = 32.7940;
        double lng = 34.9896;
        String label = "Furniture Store - Haifa";
        String geoUri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + Uri.encode(label) + ")";
        Uri mapsUri = Uri.parse(geoUri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            return;
        }

        Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        if (fallbackIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(fallbackIntent);
        } else {
            Toast.makeText(this, "No map application found", Toast.LENGTH_SHORT).show();
        }
    }
}