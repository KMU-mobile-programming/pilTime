package com.example.piltime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.piltime.Medicine.Medicine;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public UserDatabaseHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 유저 정보 삽입
    public void insertUser(User user) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user.getUserId());
        values.put("user_name", user.getUserName());
        values.put("profile_image_path", user.getProfileImagePath());

        db.insert(DatabaseHelper.USER_TABLE, null, values);
        db.close();
    }

    // 복용약 삽입
    public void insertMedicine(Medicine medicine, String userId) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("medicine_name", medicine.getName());
        values.put("dosage", medicine.getDosage());
        values.put("time", medicine.getTime());
        values.put("type", medicine.getType());
        values.put("adherence", medicine.getAdherence());

        db.insert(DatabaseHelper.MEDICINE_TABLE, null, values);
        db.close();
    }

    // 복용약 리스트 조회
    public List<Medicine> getMedicines(String userId) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.MEDICINE_TABLE,
                new String[]{"medicine_name", "dosage", "time", "type", "adherence"},
                "user_id = ?", new String[]{userId},
                null, null, null);

        List<Medicine> medicines = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String dosage = cursor.getString(1);
                String time = cursor.getString(2);
                String type = cursor.getString(3);
                double adherence = cursor.getDouble(4);

                // Medicine 객체를 그대로 사용, type에 맞는 종류를 설정
                Medicine medicine = new Medicine(name, dosage, time, type, adherence);
                medicines.add(medicine);
            }
            cursor.close();
        }
        return medicines;
    }
}
