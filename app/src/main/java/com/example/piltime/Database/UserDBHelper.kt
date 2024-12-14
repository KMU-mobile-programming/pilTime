package com.example.medtime

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import org.mindrot.jbcrypt.BCrypt

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
        if (password == null) return false // 비밀번호는 필수

        val hashedPassword = hashPassword(password) // 비밀번호 해싱
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id", id)
        contentValues.put("password", hashedPassword) // 해싱된 비밀번호 저장
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
            "Select password from users where id = ?",
            arrayOf(id)
        )
        val isValid = if (cursor.moveToFirst()) {
            val passwordIndex = cursor.getColumnIndexOrThrow("password")
            val hashedPassword = cursor.getString(passwordIndex)
            verifyPassword(password, hashedPassword) // 비밀번호 검증
        } else {
            false // 사용자 없음
        }
        cursor.close()
        MyDB.close()
        return isValid
    }

    // 사용자 휴대폰 번호 업데이트
    fun updatePhoneNumber(userId: String, newPhone: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("phone", newPhone)
        }

        val result = db.update("users", contentValues, "id = ?", arrayOf(userId))
        db.close()

        // result가 1 이상이면 성공적으로 업데이트된 것
        return result > 0
    }


    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt()) // 비밀번호 해싱
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword) // 비밀번호 검증
    }


    fun updatePassword(userId: String, oldPassword: String, newPassword: String): Boolean {
        val MyDB = this.writableDatabase

        // 기존 비밀번호 확인
        val cursor = MyDB.rawQuery("SELECT password FROM users WHERE id = ?", arrayOf(userId))
        val isUpdated = if (cursor.moveToFirst()) {
            val currentHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))

            // 기존 비밀번호 검증
            if (verifyPassword(oldPassword, currentHashedPassword)) {
                // 새 비밀번호 해싱
                val newHashedPassword = hashPassword(newPassword)

                // 업데이트 수행
                val contentValues = ContentValues()
                contentValues.put("password", newHashedPassword)
                val result = MyDB.update("users", contentValues, "id = ?", arrayOf(userId))
                result > 0 // 업데이트 성공 여부 반환
            } else {
                false // 기존 비밀번호 불일치
            }
        } else {
            false // 사용자 ID 없음
        }

        cursor.close()
        MyDB.close()
        return isUpdated
    }

    fun deleteUser(userId: String, password: String) {
        val db = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(userId)

        db.delete("users", whereClause, whereArgs)
    }


        companion object {
        const val DBNAME = "Login.db"
    }
}