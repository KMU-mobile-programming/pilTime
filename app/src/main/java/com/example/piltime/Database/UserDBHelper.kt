package com.example.medtime

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context) :
    SQLiteOpenHelper(context, DBNAME, null, 1) {

    // users 테이블 생성
    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("create Table users(id TEXT primary key, password TEXT, nick TEXT, phone TEXT)")
        // 게스트 계정 생성
        initializeGuestAccount(MyDB)
    }

    // 정보 갱신
    override fun onUpgrade(MyDB: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MyDB.execSQL("drop Table if exists users")
    }

    // 게스트 계정 초기화 (없을 경우에만 생성)
    private fun initializeGuestAccount(MyDB: SQLiteDatabase) {
        val cursor = MyDB.rawQuery("Select * from users where id = ?", arrayOf("guest"))
        val guestExists = cursor.count > 0
        cursor.close()

        if (!guestExists) {
            val contentValues = ContentValues()
            contentValues.put("id", "guest")
            contentValues.put("password", "guest123")
            contentValues.put("nick", "게스트")
            contentValues.put("phone", "")
            MyDB.insert("users", null, contentValues)
        }
    }

    fun getNickById(userId: String): String? {
        val MyDB = this.readableDatabase
        val cursor = MyDB.rawQuery("Select nick from users where id = ?", arrayOf(userId))

        return if (cursor.moveToFirst()) {
            // getColumnIndexOrThrow를 사용하여 "nick" 컬럼의 인덱스를 안전하게 가져옴
            val nickIndex = cursor.getColumnIndexOrThrow("nick")
            val nick = cursor.getString(nickIndex)
            cursor.close()
            MyDB.close()
            nick
        } else {
            cursor.close()
            MyDB.close()
            null // 유저가 없으면 null 반환
        }
    }



    // 게스트 계정이 없는 경우 생성 (외부에서 호출 가능)
    fun ensureGuestAccount() {
        val MyDB = this.writableDatabase
        initializeGuestAccount(MyDB)
        MyDB.close()
    }

    // id, password, nick, phone 삽입 (성공시 true, 실패시 false)
    fun insertData(id: String?, password: String?, nick: String?, phone: String?): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id", id)
        contentValues.put("password", password)
        contentValues.put("nick", nick)
        contentValues.put("phone", phone)
        val result = MyDB.insert("users", null, contentValues)
        MyDB.close()
        return result != -1L
    }

    // 사용자 아이디가 없으면 false, 이미 존재하면 true
    fun checkUser(id: String?): Boolean {
        val MyDB = this.readableDatabase
        val cursor = MyDB.rawQuery("Select * from users where id = ?", arrayOf(id))
        val exists = cursor.count > 0
        cursor.close()
        MyDB.close()
        return exists
    }

    // 사용자 닉네임이 없으면 false, 이미 존재하면 true
    fun checkNick(nick: String?): Boolean {
        val MyDB = this.readableDatabase
        val cursor = MyDB.rawQuery("Select * from users where nick = ?", arrayOf(nick))
        val exists = cursor.count > 0
        cursor.close()
        MyDB.close()
        return exists
    }

    // 해당 id, password가 있는지 확인 (없다면 false)
    fun checkUserpass(id: String, password: String): Boolean {
        val MyDB = this.readableDatabase
        val cursor = MyDB.rawQuery(
            "Select * from users where id = ? and password = ?",
            arrayOf(id, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        MyDB.close()
        return exists
    }

    companion object {
        const val DBNAME = "Login.db"
    }
}