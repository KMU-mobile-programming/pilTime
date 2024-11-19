package com.example.piltime.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper implements DatabaseManager {

    // 데이터베이스 이름과 버전
    private static final String DATABASE_NAME = "piltime.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 이름
    private static final String USER_TABLE = "user";
    private static final String MEDICINE_TABLE = "medicine";

    // 유저 테이블 생성 쿼리
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    "user_id TEXT PRIMARY KEY, " +
                    "user_name TEXT, " +
                    "profile_image_path TEXT)";

    // 약물 테이블 생성 쿼리
    private static final String CREATE_MEDICINE_TABLE =
            "CREATE TABLE " + MEDICINE_TABLE + " (" +
                    "medicine_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id TEXT, " +
                    "medicine_name TEXT, " +
                    "dosage TEXT, " +
                    "time TEXT, " +
                    "type TEXT, " +
                    "adherence REAL, " +
                    "FOREIGN KEY(user_id) REFERENCES " + USER_TABLE + "(user_id))";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
    }

    // 모든 복용약 데이터를 삭제하는 메서드
    public void clearAllMedicines(String userId) {
        // 데이터베이스에서 해당 사용자에 대한 모든 복용약을 삭제하는 쿼리
        SQLiteDatabase db = getWritableDatabase();
        db.delete("medicines", "user_id = ?", new String[]{userId});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINE_TABLE);
        onCreate(db);
    }

    // 유저 관련 메서드
    @Override
    public void addUser(String userId, String userName, String profileImagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("user_name", userName);
        values.put("profile_image_path", profileImagePath);
        db.insert(USER_TABLE, null, values);
    }

    @Override
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_name FROM " + USER_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                users.add(cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    @Override
    public String getUserName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_name FROM " + USER_TABLE + " WHERE user_id = ?", new String[]{userId});
        String userName = null;
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
        }
        cursor.close();
        return userName;
    }

    @Override
    public boolean userExists(String userId) {
        // userId가 null이 아니고 비어있지 않은 경우
        if (userId != null && !userId.isEmpty()) {
            return true;
        }
        return false;
    }


    // 약물 관련 메서드
    @Override
    public void addMedicine(String userId, String medicineName, String dosage, String time, String type, float adherence) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("medicine_name", medicineName);
        values.put("dosage", dosage);
        values.put("time", time);
        values.put("type", type);
        values.put("adherence", adherence);
        db.insert(MEDICINE_TABLE, null, values);
    }

    @Override
    public List<String> getAllMedicines() {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT medicine_name FROM " + MEDICINE_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                medicines.add(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medicines;
    }

    @Override
    public List<String> getMedicinesByUserId(String userId) {
        List<String> medicines = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT medicine_name FROM " + MEDICINE_TABLE + " WHERE user_id = ?", new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                medicines.add(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medicines;
    }
}
