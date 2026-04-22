package com.example.afinal;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public final class NavHelper {
    private NavHelper() {
    }

    public static void bindTopBar(Activity activity) {
        TextView txtAppName = activity.findViewById(R.id.txtAppName);
        if (txtAppName != null) {
            String username = SessionManager.getUsername(activity);
            if (!username.isEmpty()) {
                txtAppName.setText("Luxury Store - " + username);
            } else {
                txtAppName.setText("Luxury Store");
            }
        }

        bind(activity, R.id.txtHome, home.class);
        bind(activity, R.id.txtLogin, login.class);
        bind(activity, R.id.txtCart, cart.class);
        bind(activity, R.id.txtAbout, about.class);
    }

    private static void bind(Activity activity, int id, Class<?> target) {
        View view = activity.findViewById(id);
        if (view instanceof TextView) {
            view.setOnClickListener(v -> {
                Intent intent = new Intent(activity, target);
                activity.startActivity(intent);
            });
        }
    }
}
