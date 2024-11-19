package com.example.piltime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 이름과 버전
    private static final String DATABASE_NAME = "piltime.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 이름
    public static final String USER_TABLE = "user";
    public static final String MEDICINE_TABLE = "medicine";

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_MEDICINE_TABLE);
    }

    // 데이터베이스 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MEDICINE_TABLE);
        onCreate(db);
    }
}
