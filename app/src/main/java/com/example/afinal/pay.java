package com.example.afinal;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;

public class pay extends AppCompatActivity {

    private TextView txtTimer;
    private CountDownTimer countDownTimer;
    private static final String CHANNEL_ID = "order_confirmation_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        NavHelper.bindTopBar(this);
        requestNotificationPermission();

        txtTimer = findViewById(R.id.txtTimer);
        TextView total = findViewById(R.id.txtTotalAmount);
        
        total.setText("$370");

        findViewById(R.id.btnGooglePay).setOnClickListener(v -> showGooglePayDialog());
        findViewById(R.id.btnPayPal).setOnClickListener(v -> showPayPalDialog());
        findViewById(R.id.btnVisa).setOnClickListener(v -> showVisaDialog());

        startTimer();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(600000, 1000) { // 10 minutes
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "Time left: %02d:%02d", minutes, seconds);
                txtTimer.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                txtTimer.setText("Time left: 00:00");
                Toast.makeText(pay.this, "Payment time expired!", Toast.LENGTH_LONG).show();
                finish();
            }
        }.start();
    }

    private void showGooglePayDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Google Gmail");
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        new AlertDialog.Builder(this)
                .setTitle("Google Pay")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (emailInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showPayPalDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("PayPal Email");
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(passwordInput);

        new AlertDialog.Builder(this)
                .setTitle("PayPal")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (emailInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showVisaDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText nameInput = new EditText(this);
        nameInput.setHint("Cardholder Name");
        layout.addView(nameInput);

        final EditText numberInput = new EditText(this);
        numberInput.setHint("Card Number");
        numberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(numberInput);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        final EditText dateInput = new EditText(this);
        dateInput.setHint("MM/YY");
        dateInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(dateInput);

        final EditText cvvInput = new EditText(this);
        cvvInput.setHint("CVV");
        cvvInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        cvvInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        row.addView(cvvInput);

        layout.addView(row);

        new AlertDialog.Builder(this)
                .setTitle("Visa / Credit Card")
                .setView(layout)
                .setPositiveButton("Pay", (dialog, which) -> {
                    if (nameInput.getText().toString().isEmpty() || numberInput.getText().toString().isEmpty() ||
                        dateInput.getText().toString().isEmpty() || cvvInput.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        processPayment();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void processPayment() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        // إرسال الإشعار فور نجاح العملية
        sendOrderNotification();
        
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
        try {
            CartManager.clear(this);
        } catch (Exception ignored) {}
        finish();
    }

    private void sendOrderNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // إنشاء القناة (مطلوب لأندرويد 8 فما فوق)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Order Confirmations",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for successful orders");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // تحديد ما سيحدث عند الضغط على الإشعار (فتح صفحة المتجر)
        Intent intent = new Intent(this, home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // بناء محتوى الإشعار
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Order Confirmed! 🎉")
                .setContentText("Your purchase was successful. We are preparing your furniture!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // عرض الإشعار
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
