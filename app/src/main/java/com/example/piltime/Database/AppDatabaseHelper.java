package com.example.piltime.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 이름과 버전
    private static final String DATABASE_NAME = "piltime.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 이름
    private static final String MEDICINE_TABLE = "medicine";

    // 약물 테이블 생성 쿼리
    private static final String CREATE_MEDICINE_TABLE =
            "CREATE TABLE " + MEDICINE_TABLE + " (" +
                    "medicine_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id TEXT, " +
                    "medicine_name TEXT, " +
                    "dosage TEXT, " +
                    "time TEXT, " +
                    "type TEXT, " +
                    "adherence REAL)";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEDICINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINE_TABLE);
        onCreate(db);
    }

    // 약물 관련 메서드
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

    public void clearAllMedicines(String userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MEDICINE_TABLE, "user_id = ?", new String[]{userId});
    }

    public void removeMedicine(String userId, String medicineName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MEDICINE_TABLE, "user_id = ? AND medicine_name = ?", new String[]{userId, medicineName});
    }
}
