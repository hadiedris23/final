package com.example.afinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_PRICE = "extra_price";
    public static final String EXTRA_RATING = "extra_rating";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_MAIN_IMAGE = "extra_main_image";
    public static final String EXTRA_GALLERY = "extra_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        NavHelper.bindTopBar(this);

        String name = getIntent().getStringExtra(EXTRA_NAME);
        String price = getIntent().getStringExtra(EXTRA_PRICE);
        String rating = getIntent().getStringExtra(EXTRA_RATING);
        String desc = getIntent().getStringExtra(EXTRA_DESC);
        int mainImage = getIntent().getIntExtra(EXTRA_MAIN_IMAGE, R.drawable.beedroom);
        int[] gallery = getIntent().getIntArrayExtra(EXTRA_GALLERY);
        if (gallery == null || gallery.length < 4) {
            gallery = new int[]{mainImage, mainImage, mainImage, mainImage};
        }

        ImageView imgMain = findViewById(R.id.imgMain);
        ImageView thumb1 = findViewById(R.id.imgThumb1);
        ImageView thumb2 = findViewById(R.id.imgThumb2);
        ImageView thumb3 = findViewById(R.id.imgThumb3);
        ImageView thumb4 = findViewById(R.id.imgThumb4);
        TextView txtName = findViewById(R.id.txtProductName);
        TextView txtPrice = findViewById(R.id.txtProductPrice);
        TextView txtRating = findViewById(R.id.txtProductRating);
        TextView txtDesc = findViewById(R.id.txtProductDesc);
        Button btnAdd = findViewById(R.id.btnAddToCart);

        imgMain.setImageResource(mainImage);
        thumb1.setImageResource(gallery[0]);
        thumb2.setImageResource(gallery[1]);
        thumb3.setImageResource(gallery[2]);
        thumb4.setImageResource(gallery[3]);
        txtName.setText(name);
        txtPrice.setText(price);
        txtRating.setText(rating == null ? "Rating: 4.5/5" : rating);
        txtDesc.setText(desc);

        int[] finalGallery = gallery;
        thumb1.setOnClickListener(v -> imgMain.setImageResource(finalGallery[0]));
        thumb2.setOnClickListener(v -> imgMain.setImageResource(finalGallery[1]));
        thumb3.setOnClickListener(v -> imgMain.setImageResource(finalGallery[2]));
        thumb4.setOnClickListener(v -> imgMain.setImageResource(finalGallery[3]));

        btnAdd.setOnClickListener(v -> {
            CartManager.addItem(this, name, price);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}
