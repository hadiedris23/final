package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    private static final String ARG_CATEGORY = "extra_category";

    private static final int[] CARD_IDS = {R.id.cardProduct1, R.id.cardProduct2, R.id.cardProduct3, R.id.cardProduct4};
    private static final int[] IMAGE_IDS = {R.id.imgProduct1, R.id.imgProduct2, R.id.imgProduct3, R.id.imgProduct4};
    private static final int[] NAME_IDS = {R.id.txtName1, R.id.txtName2, R.id.txtName3, R.id.txtName4};
    private static final int[] DESC_IDS = {R.id.txtDesc1, R.id.txtDesc2, R.id.txtDesc3, R.id.txtDesc4};
    private static final int[] RATING_IDS = {R.id.txtRating1, R.id.txtRating2, R.id.txtRating3, R.id.txtRating4};
    private static final int[] PRICE_IDS = {R.id.txtPrice1, R.id.txtPrice2, R.id.txtPrice3, R.id.txtPrice4};
    private static final int[] ADD_IDS = {R.id.btnAdd1, R.id.btnAdd2, R.id.btnAdd3, R.id.btnAdd4};

    private String categoryName;

    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category, container, false);
        
        TextView title = view.findViewById(R.id.txtCategoryTitle);
        title.setText(categoryName != null ? categoryName : "Bedroom");

        bindProducts(view, categoryName != null ? categoryName : "Bedroom");
        
        return view;
    }

    private void bindProducts(View root, String category) {
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

            ImageView image = root.findViewById(IMAGE_IDS[i]);
            TextView nameView = root.findViewById(NAME_IDS[i]);
            TextView descView = root.findViewById(DESC_IDS[i]);
            TextView ratingView = root.findViewById(RATING_IDS[i]);
            TextView priceView = root.findViewById(PRICE_IDS[i]);
            Button addButton = root.findViewById(ADD_IDS[i]);
            CardView card = root.findViewById(CARD_IDS[i]);

            image.setImageResource(imageRes);
            nameView.setText(name);
            descView.setText(desc);
            ratingView.setText(rating);
            priceView.setText(price);

            addButton.setOnClickListener(v -> {
                CartManager.addItem(requireContext(), name, price);
                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            });

            card.setOnClickListener(v -> openDetails(name, price, rating, desc, imageRes, gallery));
            image.setOnClickListener(v -> openDetails(name, price, rating, desc, imageRes, gallery));
        }
    }

    private int[] loadGallery(String categoryKey, int productNumber, int fallback) {
        int[] images = new int[4];
        for (int i = 0; i < 4; i++) {
            String fileName = categoryKey + "_p" + productNumber + "_" + (i + 1);
            int resId = getResources().getIdentifier(fileName, "drawable", requireActivity().getPackageName());
            images[i] = resId == 0 ? fallback : resId;
        }
        return images;
    }

    private String normalizeCategoryKey(String category) {
        String normalized = category.toLowerCase();
        normalized = normalized.replace(" ", "");
        normalized = normalized.replace("/", "");
        if (normalized.contains("living")) return "livingroom";
        if (normalized.contains("chair")) return "chair";
        return normalized;
    }

    private void openDetails(String name, String price, String rating, String desc, int mainImage, int[] gallery) {
        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
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
        if ("chair".equals(key)) return index <= 2 ? (500 + ((index - 1) * 250)) : (1000 + ((index - 3) * 1000));
        if ("kitchen".equals(key)) return 1500 + ((index - 1) * 500);
        if ("livingroom".equals(key)) return 1000 + ((index - 1) * 333);
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
        if (normalized.contains("kitchen")) return R.drawable.kitchen;
        if (normalized.contains("living")) return R.drawable.livingroom;
        if (normalized.contains("chair")) return R.drawable.chair;
        return R.drawable.beedroom;
    }

    private String productName(String category, int index) {
        String key = normalizeCategoryKey(category);
        if ("chair".equals(key)) return new String[]{"Chair Classic", "Chair Modern", "Sofa Compact", "Sofa Family"}[index - 1];
        if ("kitchen".equals(key)) return new String[]{"Dining Table S", "Dining Table M", "Dining Table L", "Dining Table XL"}[index - 1];
        if ("livingroom".equals(key)) return new String[]{"Sofa One", "Sofa Two", "Sofa Three", "Sofa Premium"}[index - 1];
        return new String[]{"Bed Frame", "Wardrobe", "Night Stand", "Dresser"}[index - 1];
    }
}