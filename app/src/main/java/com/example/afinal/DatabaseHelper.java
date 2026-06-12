package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    // رفع الإصدار إلى 5 لضمان إعادة تهيئة الجداول
    private static final int DATABASE_VERSION = 5;
    
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // فتح قاعدة البيانات عند إنشاء الكائن وإبقاؤها مفتوحة للـ Inspector
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USERNAME + " TEXT, " +
                    COL_PASSWORD + " TEXT)";
            db.execSQL(createTable);
            Log.d("DB_INFO", "تم إنشاء الجدول بنجاح - الإصدار 5");
        } catch (Exception e) {
            Log.e("DB_ERROR", "خطأ في إنشاء الجدول", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        Log.d("DB_INFO", "تم تحديث قاعدة البيانات بنجاح");
    }

    public boolean addUser(String username, String password) {
        try {
            // لا نستخدم try-with-resources هنا لـ db ليبقى الاتصال مفتوحاً
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_USERNAME, username);
            values.put(COL_PASSWORD, password);
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error adding user", e);
            return false;
        }
    }

    public boolean checkUser(String username, String password) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            // نغلق الـ Cursor فقط
            try (Cursor cursor = db.rawQuery(query, new String[]{username, password})) {
                return cursor != null && cursor.getCount() > 0;
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error checking user", e);
            return false;
        }
    }

    public boolean userExists(String username) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=?";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            try (Cursor cursor = db.rawQuery(query, new String[]{username})) {
                return cursor != null && cursor.getCount() > 0;
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error checking existence", e);
            return false;
        }
    }

    public String getAllUsersAsString() {
        StringBuilder builder = new StringBuilder();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int userIdx = cursor.getColumnIndexOrThrow(COL_USERNAME);
                    int passIdx = cursor.getColumnIndexOrThrow(COL_PASSWORD);
                    do {
                        String user = cursor.getString(userIdx);
                        String pass = cursor.getString(passIdx);
                        builder.append("👤 ").append(user).append("  🔑 ").append(pass).append("\n\n");
                    } while (cursor.moveToNext());
                } else {
                    builder.append("قاعدة البيانات فارغة.");
                }
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error reading all users", e);
            builder.append("خطأ في قراءة البيانات.");
        }
        return builder.toString();
    }
}
