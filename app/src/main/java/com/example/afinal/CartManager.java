package com.example.afinal;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public final class CartManager {
    private static final String PREFS = "cart_store";
    private static final String KEY_ITEMS = "items";

    private CartManager() {
    }

    public static void addItem(Context context, String name, String price) {
        List<String> items = getItems(context);
        items.add(name + " - " + price);
        saveItems(context, items);
    }

    public static List<String> getItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String raw = prefs.getString(KEY_ITEMS, "[]");
        List<String> items = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(raw);
            for (int i = 0; i < array.length(); i++) {
                items.add(array.getString(i));
            }
        } catch (JSONException ignored) {
        }
        return items;
    }

    public static void removeAt(Context context, int index) {
        List<String> items = getItems(context);
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            saveItems(context, items);
        }
    }

    public static void clear(Context context) {
        saveItems(context, new ArrayList<>());
    }

    private static void saveItems(Context context, List<String> items) {
        JSONArray array = new JSONArray();
        for (String item : items) {
            array.put(item);
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_ITEMS, array.toString())
                .apply();
    }
}
