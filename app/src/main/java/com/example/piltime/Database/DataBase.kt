package com.example.piltime.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import android.util.Log
import com.example.piltime.model.Medicine
import java.sql.SQLException

// Post 데이터 클래스
data class Post(
    val id: Long,
    val userId: String,
    val title: String,
    val content: String,
    val imageUri: String?
)

// Comment 데이터 클래스
data class Comment(
    val id: Long,
    val postId: Long,
    val userId: String,
    val content: String,
    val createdAt: String
)

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private fun hashPassword(password: String): String {
        val salt = "piltime_salt".toByteArray()
        val iterations = 10000
        val keyLength = 256
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return hash.joinToString("") { "%02x".format(it) }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // users 테이블 생성
        db.execSQL("""
           CREATE TABLE $USERS_TABLE (
               id TEXT PRIMARY KEY,
               password TEXT,
               nick TEXT,
               phone TEXT
           )
       """.trimIndent())

        // 게스트 계정 생성
        db.execSQL("""
           INSERT INTO $USERS_TABLE (id, password, nick, phone)
           VALUES ('guest', 'guest123', '게스트', '')
       """.trimIndent())

        // medicine 테이블 생성
        db.execSQL("""
            CREATE TABLE $MEDICINE_TABLE (
                user_id TEXT NOT NULL,
                medicine_name TEXT NOT NULL,  -- 약 이름은 NULL이 아니어야 함
                dosage TEXT DEFAULT "",         -- 기본값: 빈 문자열
                time TEXT DEFAULT "",           -- 기본값: 빈 문자열
                type TEXT DEFAULT "영양제",     -- 기본값: "영양제"
                adherence REAL DEFAULT 0.0,     -- 기본값: 0.0
                days TEXT DEFAULT "",           -- 기본값: 빈 문자열
                notification_time TEXT DEFAULT "", -- 기본값: 빈 문자열
                notification_days TEXT DEFAULT "",  -- 기본값: 빈 문자열
                FOREIGN KEY(user_id) REFERENCES $USERS_TABLE(id)
            );
       """.trimIndent())

        // posts 테이블 생성
        db.execSQL("""
            CREATE TABLE $POSTS_TABLE (
                post_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id TEXT,
                title TEXT,
                content TEXT,
                image_uri TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES $USERS_TABLE(id)
            )
        """.trimIndent())

        // comments 테이블 생성
        db.execSQL("""
            CREATE TABLE $COMMENTS_TABLE (
                comment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                post_id INTEGER,
                user_id TEXT,
                content TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(post_id) REFERENCES $POSTS_TABLE(post_id),
                FOREIGN KEY(user_id) REFERENCES $USERS_TABLE(id)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $COMMENTS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $POSTS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $MEDICINE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USERS_TABLE")
        onCreate(db)
    }
    // 유저 관련 매소드
    private fun initializeGuestAccount(db: SQLiteDatabase) {
        val stmt = db.compileStatement(
            "SELECT COUNT(*) FROM $USERS_TABLE WHERE id = ?"
        )
        stmt.bindString(1, "guest")
        val count = stmt.simpleQueryForLong()
        stmt.close()

        if (count == 0L) {
            val insertStmt = db.compileStatement(
                "INSERT INTO $USERS_TABLE (id, password, nick, phone) VALUES (?, ?, ?, ?)"
            )
            insertStmt.bindString(1, "guest")
            insertStmt.bindString(2, hashPassword("guest123"))
            insertStmt.bindString(3, "게스트")
            insertStmt.bindString(4, "")
            insertStmt.executeInsert()
            insertStmt.close()
        }
    }

    fun getNickById(userId: String): String? {
        val db = this.readableDatabase
        var nick: String? = null
        try {
            Log.d("DataBase", "Querying nickname for userId: $userId")
            val cursor = db.rawQuery(
                "SELECT nick FROM $USERS_TABLE WHERE id = ?",
                arrayOf(userId)
            )
            if (cursor.moveToFirst()) {
                nick = cursor.getString(cursor.getColumnIndexOrThrow("nick"))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DataBase", "Error fetching nickname for userId: $userId", e)
        } finally {
            db.close()
        }
        return nick ?: "게스트" // 기본값 설정
    }


    fun insertUser(id: String?, password: String?, nick: String?, phone: String?): Boolean {
        if (id == null || password == null) return false
        val db = this.writableDatabase
        val stmt = db.compileStatement(
            "INSERT INTO $USERS_TABLE (id, password, nick, phone) VALUES (?, ?, ?, ?)"
        )
        stmt.bindString(1, id)
        stmt.bindString(2, hashPassword(password))
        stmt.bindString(3, nick)
        stmt.bindString(4, phone)
        val result = stmt.executeInsert()
        stmt.close()
        db.close()
        return result != -1L
    }


    fun checkUser(id: String?): Boolean {
        if (id == null) return false
        val db = this.readableDatabase
        val stmt = db.compileStatement(
            "SELECT COUNT(*) FROM $USERS_TABLE WHERE id = ?"
        )
        stmt.bindString(1, id)
        val count = stmt.simpleQueryForLong()
        stmt.close()
        db.close()
        return count > 0
    }

    fun checkNick(nick: String?): Boolean {
        if (nick == null) return false
        val db = this.readableDatabase
        val stmt = db.compileStatement(
            "SELECT COUNT(*) FROM $USERS_TABLE WHERE nick = ?"
        )
        stmt.bindString(1, nick)
        val count = stmt.simpleQueryForLong()
        stmt.close()
        db.close()
        return count > 0
    }

    fun checkUserpass(id: String, password: String): Boolean {
        val db = this.readableDatabase
        val stmt = db.compileStatement(
            "SELECT COUNT(*) FROM $USERS_TABLE WHERE id = ? AND password = ?"
        )
        stmt.bindString(1, id)
        stmt.bindString(2, hashPassword(password))
        val count = stmt.simpleQueryForLong()
        stmt.close()
        db.close()
        return count > 0
    }

    // 게시글 관련 메서드들
    fun getAllPosts(): List<Post> {
        val posts = mutableListOf<Post>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $POSTS_TABLE ORDER BY created_at DESC",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                posts.add(Post(
                    cursor.getLong(cursor.getColumnIndexOrThrow("post_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("content")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_uri"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return posts
    }

    fun addPost(userId: String, title: String, content: String, imageUri: String?): Long {
        val db = writableDatabase
        val stmt = db.compileStatement("""
            INSERT INTO $POSTS_TABLE (user_id, title, content, image_uri)
            VALUES (?, ?, ?, ?)
        """.trimIndent())

        stmt.bindString(1, userId)
        stmt.bindString(2, title)
        stmt.bindString(3, content)
        stmt.bindString(4, imageUri)

        val id = stmt.executeInsert()
        stmt.close()
        db.close()
        return id
    }
    fun updatePost(postId: Long, title: String, content: String, imageUri: String?): Boolean {
        val db = writableDatabase
        val stmt = db.compileStatement("""
        UPDATE $POSTS_TABLE 
        SET title = ?, content = ?, image_uri = ?
        WHERE post_id = ?
    """.trimIndent())

        stmt.bindString(1, title)
        stmt.bindString(2, content)
        stmt.bindString(3, imageUri)
        stmt.bindLong(4, postId+1)

        val result = stmt.executeUpdateDelete()
        stmt.close()
        db.close()
        return result > 0
    }
//게시글 삭제
    fun deletePost(postId: Long, userId: String): Boolean {
        val db = writableDatabase
        val stmt = db.compileStatement(
            "DELETE FROM $POSTS_TABLE WHERE post_id = ? AND user_id = ?"
        )
        stmt.bindLong(1, postId+1)
        stmt.bindString(2, userId)

        val result = stmt.executeUpdateDelete()
        stmt.close()
        db.close()
        return result > 0
    }

    // 댓글 관련 메서드들
    fun addComment(postId: Long, userId: String, content: String): Long {
        val db = writableDatabase
        val stmt = db.compileStatement("""
            INSERT INTO $COMMENTS_TABLE (post_id, user_id, content)
            VALUES (?, ?, ?)
        """.trimIndent())

        stmt.bindLong(1, postId)
        stmt.bindString(2, userId)
        stmt.bindString(3, content)

        val id = stmt.executeInsert()
        stmt.close()
        db.close()
        return id
    }

    fun getCommentsByPostId(postId: Long): List<Comment> {
        val comments = mutableListOf<Comment>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT * FROM $COMMENTS_TABLE 
            WHERE post_id = ? 
            ORDER BY created_at DESC
            """.trimIndent(),
            arrayOf(postId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                comments.add(Comment(
                    cursor.getLong(cursor.getColumnIndexOrThrow("comment_id")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("post_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("content")),
                    cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return comments
    }

    fun deleteComment(commentId: Long, userId: String): Boolean {
        val db = writableDatabase
        val stmt = db.compileStatement(
            "DELETE FROM $COMMENTS_TABLE WHERE comment_id = ? AND user_id = ?"
        )
        stmt.bindLong(1, commentId)
        stmt.bindString(2, userId)

        val result = stmt.executeUpdateDelete()
        stmt.close()
        fun addMedicine(medicine: Medicine) {
            val db = writableDatabase
            val contentValues = ContentValues().apply {
                put("medicine_name", medicine.name)
                put("dosage", medicine.dosage)
                put("type", medicine.type)
                put("adherence", medicine.adherence)
                put("days", medicine.selectedDays.joinToString(",")) // 요일 리스트를 문자열로 저장
                put("notification_time", medicine.hourList.zip(medicine.minuteList).joinToString(",") { "${it.first}:${it.second}" }) // 시간과 분을 조합
                put("start_date", medicine.startDateString) // 복용 시작 날짜 저장
            }

            db.insert("medicine", null, contentValues)
            db.close()
        }
        db.close()
        return result > 0
    }

    // 약 관련 매소트

    fun addMedicine(medicine: Medicine) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("user_id", medicine.user_id) // 사용자 ID 추가
            put("medicine_name", medicine.name) // 약 이름
            put("dosage", medicine.dosage) // 복용량
            put("time", "") // 기본값으로 빈 문자열
            put("type", medicine.type) // 약의 종류
            put("adherence", medicine.adherence) // 복용율
            put("days", medicine.selectedDays.joinToString(",")) // 요일 리스트를 문자열로 저장
            put("notification_time", medicine.hourList.zip(medicine.minuteList).joinToString(",") { "${it.first}:${it.second}" }) // 시간과 분을 조합
            put("notification_days", "") // 기본값으로 빈 문자열
        }

        try {
            val result = db.insert("medicine", null, contentValues)
            if (result == -1L) {
                // 삽입 실패 시 로그에 오류 메시지 추가
                Log.e("DBHelper", "약 정보 저장 실패: 삽입된 데이터 - $contentValues")
            } else {
                Log.d("DBHelper", "약 정보 저장 성공 ${medicine.user_id}, ID: $result")
            }
        } catch (e: SQLException) {
            Log.e("DBHelper", "SQL 예외 발생: ${e.message}")
        } catch (e: Exception) {
            Log.e("DBHelper", "약 정보 저장 중 예외 발생: ${e.message}")
        } finally {
            db.close()
        }

        // 저장된 약의 개수 출력
        val count = getMedicineCount()
        Log.d("DBHelper", "저장된 약의 개수: $count")
    }


    fun getMedicineCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM medicine", null)
        var count = 0

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }



    fun getMedicinesByUserId(userId: String): List<String> {
        val medicines = mutableListOf<String>()
        val db = this.readableDatabase

        if (userId.isBlank()) {
            Log.e("DBHelper", "유효하지 않은 userId입니다.")
            return medicines
        }

        try {
            Log.d("DBHelper", "쿼리 실행: 사용자 ID - $userId")
            val cursor = db.rawQuery(
                "SELECT medicine_name FROM $MEDICINE_TABLE WHERE user_id = ?",
                arrayOf(userId)
            )

            if (cursor.moveToFirst()) {
                do {
                    medicines.add(cursor.getString(cursor.getColumnIndexOrThrow("medicine_name")))
                } while (cursor.moveToNext())
            } else {
                Log.d("DBHelper", "해당 사용자 ID에 대한 약물이 없습니다.")
            }

            cursor.close()
        } catch (e: SQLException) {
            Log.e("DBHelper", "SQL 오류 발생: ${e.message}")
        } finally {
            db.close()
        }

        Log.d("DBHelper", "Fetched medicine names: $medicines")
        return medicines
    }


    fun clearAllMedicines(userId: String) {
        val db = writableDatabase
        val stmt = db.compileStatement(
            "DELETE FROM $MEDICINE_TABLE WHERE user_id = ?"
        )
        stmt.bindString(1, userId)
        stmt.executeUpdateDelete()
        stmt.close()
        db.close()
    }

    fun removeMedicine(userId: String, medicineName: String) {
        val db = writableDatabase
        val stmt = db.compileStatement(
            "DELETE FROM $MEDICINE_TABLE WHERE user_id = ? AND medicine_name = ?"
        )
        stmt.bindString(1, userId)
        stmt.bindString(2, medicineName)
        stmt.executeUpdateDelete()
        stmt.close()
        db.close()
    }




    companion object {
        const val DATABASE_NAME = "medtime.db"
        const val DATABASE_VERSION = 4
        const val USERS_TABLE = "users"
        const val MEDICINE_TABLE = "medicine"
        const val POSTS_TABLE = "posts"
        const val COMMENTS_TABLE = "comments"
    }
}