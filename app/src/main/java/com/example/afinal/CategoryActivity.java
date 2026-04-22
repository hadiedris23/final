package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "extra_category";

    private static final int[] CARD_IDS = {R.id.cardProduct1, R.id.cardProduct2, R.id.cardProduct3, R.id.cardProduct4};
    private static final int[] IMAGE_IDS = {R.id.imgProduct1, R.id.imgProduct2, R.id.imgProduct3, R.id.imgProduct4};
    private static final int[] NAME_IDS = {R.id.txtName1, R.id.txtName2, R.id.txtName3, R.id.txtName4};
    private static final int[] DESC_IDS = {R.id.txtDesc1, R.id.txtDesc2, R.id.txtDesc3, R.id.txtDesc4};
    private static final int[] RATING_IDS = {R.id.txtRating1, R.id.txtRating2, R.id.txtRating3, R.id.txtRating4};
    private static final int[] PRICE_IDS = {R.id.txtPrice1, R.id.txtPrice2, R.id.txtPrice3, R.id.txtPrice4};
    private static final int[] ADD_IDS = {R.id.btnAdd1, R.id.btnAdd2, R.id.btnAdd3, R.id.btnAdd4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        NavHelper.bindTopBar(this);

        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (category == null || category.trim().isEmpty()) {
            category = "Bedroom";
        }

        TextView title = findViewById(R.id.txtCategoryTitle);
        title.setText(category);

        bindProducts(category);
    }

    private void bindProducts(String category) {
        String key = normalizeCategoryKey(category);
        for (int i = 0; i < 4; i++) {
            int index = i + 1;
            String name = productName(category, index);
            String price = "₪" + suitablePrice(category, index);
            String rating = "Rating: " + suitableRating(category, index) + "/5";
            String desc = "Modern " + category + " item with premium finish and comfortable style.";
            int fallbackImage = imageForCategory(category);
            int[] gallery = loadGallery(key, index, fallbackImage);
            int imageRes = gallery[0];

            ImageView image = findViewById(IMAGE_IDS[i]);
            TextView nameView = findViewById(NAME_IDS[i]);
            TextView descView = findViewById(DESC_IDS[i]);
            TextView ratingView = findViewById(RATING_IDS[i]);
            TextView priceView = findViewById(PRICE_IDS[i]);
            Button addButton = findViewById(ADD_IDS[i]);
            CardView card = findViewById(CARD_IDS[i]);

            image.setImageResource(imageRes);
            nameView.setText(name);
            descView.setText(desc);
            ratingView.setText(rating);
            priceView.setText(price);

            addButton.setOnClickListener(v -> {
                CartManager.addItem(this, name, price);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            });

            card.setOnClickListener(v -> openDetails(name, price, rating, desc, imageRes, gallery));
            image.setOnClickListener(v -> openDetails(name, price, rating, desc, imageRes, gallery));
        }
    }

    private int[] loadGallery(String categoryKey, int productNumber, int fallback) {
        int[] images = new int[4];
        for (int i = 0; i < 4; i++) {
            String fileName = categoryKey + "_p" + productNumber + "_" + (i + 1);
            int resId = getResources().getIdentifier(fileName, "drawable", getPackageName());
            images[i] = resId == 0 ? fallback : resId;
        }
        return images;
    }

    private String normalizeCategoryKey(String category) {
        String normalized = category.toLowerCase();
        normalized = normalized.replace(" ", "");
        normalized = normalized.replace("/", "");
        if (normalized.contains("living")) {
            return "livingroom";
        }
        if (normalized.contains("chair")) {
            return "chair";
        }
        return normalized;
    }

    private void openDetails(String name, String price, String rating, String desc, int mainImage, int[] gallery) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_NAME, name);
        intent.putExtra(ProductDetailActivity.EXTRA_PRICE, price);
        intent.putExtra(ProductDetailActivity.EXTRA_RATING, rating);
        intent.putExtra(ProductDetailActivity.EXTRA_DESC, desc);
        intent.putExtra(ProductDetailActivity.EXTRA_MAIN_IMAGE, mainImage);
        intent.putExtra(ProductDetailActivity.EXTRA_GALLERY, gallery);
        startActivity(intent);
    }

    private int suitablePrice(String category, int index) {
        String key = normalizeCategoryKey(category);
        if ("chair".equals(key)) {
            // Chair: 500-1000, Sofa: 1000-2000
            return index <= 2 ? (500 + ((index - 1) * 250)) : (1000 + ((index - 3) * 1000));
        }
        if ("kitchen".equals(key)) {
            // Tables: 1500-3000
            return 1500 + ((index - 1) * 500);
        }
        if ("livingroom".equals(key)) {
            // Sofas in living room: 1000-2000
            return 1000 + ((index - 1) * 333);
        }
        // Bedroom default
        return 900 + ((index - 1) * 250);
    }

    private String suitableRating(String category, int index) {
        String key = normalizeCategoryKey(category);
        if ("bedroom".equals(key)) return new String[]{"4.7", "4.6", "4.8", "4.5"}[index - 1];
        if ("kitchen".equals(key)) return new String[]{"4.5", "4.4", "4.6", "4.3"}[index - 1];
        if ("livingroom".equals(key)) return new String[]{"4.8", "4.7", "4.9", "4.6"}[index - 1];
        return new String[]{"4.4", "4.5", "4.3", "4.6"}[index - 1];
    }

    private int imageForCategory(String category) {
        String normalized = category.toLowerCase();
        if (normalized.contains("kitchen")) {
            return R.drawable.kitchen;
        }
        if (normalized.contains("living")) {
            return R.drawable.livingroom;
        }
        if (normalized.contains("chair")) {
            return R.drawable.chair;
        }
        return R.drawable.beedroom;
    }

    private String productName(String category, int index) {
        String key = normalizeCategoryKey(category);
        if ("chair".equals(key)) {
            String[] names = {"Chair Classic", "Chair Modern", "Sofa Compact", "Sofa Family"};
            return names[index - 1];
        }
        if ("kitchen".equals(key)) {
            String[] names = {"Dining Table S", "Dining Table M", "Dining Table L", "Dining Table XL"};
            return names[index - 1];
        }
        if ("livingroom".equals(key)) {
            String[] names = {"Sofa One", "Sofa Two", "Sofa Three", "Sofa Premium"};
            return names[index - 1];
        }
        String[] names = {"Bed Frame", "Wardrobe", "Night Stand", "Dresser"};
        return names[index - 1];
    }
}
