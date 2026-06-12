package com.example.afinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavHelper.bindTopBar(this);

        findViewById(R.id.cardBedroom).setOnClickListener(v -> loadCategoryFragment("Bedroom"));
        findViewById(R.id.cardKitchen).setOnClickListener(v -> loadCategoryFragment("Kitchen"));
        findViewById(R.id.cardLivingRoom).setOnClickListener(v -> loadCategoryFragment("Living Room"));
        findViewById(R.id.cardChair).setOnClickListener(v -> loadCategoryFragment("Chair / Sofa"));

        Toast.makeText(this, "Welcome to Luxury Store", Toast.LENGTH_SHORT).show();
    }

    private void loadCategoryFragment(String category) {
        // Hide the home scroll view to show the fragment
        findViewById(R.id.home_scroll_view).setVisibility(View.GONE);

        CategoryFragment fragment = CategoryFragment.newInstance(category);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // Show the home scroll view again when coming back
            findViewById(R.id.home_scroll_view).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}