package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductActivity extends AppCompatActivity {

    ImageView imgCategory;
    TextView txtCategoryTitle, txtCategoryDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        imgCategory = findViewById(R.id.imgCategory);
        txtCategoryTitle = findViewById(R.id.txtCategoryTitle);
        txtCategoryDesc = findViewById(R.id.txtCategoryDesc);

        // استلام البيانات من الصفحة الرئيسية
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        int imageRes = intent.getIntExtra("imageRes", R.drawable.background_furniture);

        // وضع البيانات في الLayout
        txtCategoryTitle.setText(title);
        txtCategoryDesc.setText(desc);
        imgCategory.setImageResource(imageRes);
    }
}