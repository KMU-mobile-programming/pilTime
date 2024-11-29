package com.example.piltime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medtime.UserDBHelper


class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: ImageButton
    private lateinit var editTextId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnGuestLogin: Button
    private var DB: UserDBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            DB = UserDBHelper(this)

            // View 초기화
            btnLogin = findViewById(R.id.btnLogin)
            editTextId = findViewById(R.id.editTextId)
            editTextPassword = findViewById(R.id.editTextPassword)
            btnRegister = findViewById(R.id.btnRegister)
            btnGuestLogin = findViewById(R.id.btnGuestLogin)

            // 일반 로그인 버튼 클릭
            btnLogin.setOnClickListener {
                val user = editTextId.text.toString()
                val pass = editTextPassword.text.toString()

                // 빈칸 제출시 Toast
                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val checkUserpass = DB?.checkUserpass(user, pass) ?: false
                    // id 와 password 일치시
                    if (checkUserpass) {
                        // 로그인 성공 시 사용자 정보를 HomeActivity로 전달
                        val intent = Intent(applicationContext, HomeActivity::class.java).apply {
                            putExtra("USER_ID", user)
                            putExtra("IS_GUEST", false)  // 일반 로그인은 게스트가 아님
                        }
                        Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish() // 현재 액티비티 종료
                    } else {
                        Toast.makeText(this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // 게스트 로그인 버튼 클릭
            btnGuestLogin.setOnClickListener {
                DB?.ensureGuestAccount() // 게스트 계정이 없는 경우를 대비해 확인
                val checkUserpass = DB?.checkUserpass("guest", "guest123") ?: false
                if (checkUserpass) {
                    // 사용자 ID를 SharedPreferences에 저장
                    saveUserIdToPreferences("guest")

                    val intent = Intent(applicationContext, HomeActivity::class.java).apply {
                        putExtra("USER_ID", "guest")
                        putExtra("IS_GUEST", true)
                    }
                    Toast.makeText(this, "게스트로 로그인되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "게스트 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 회원가입 버튼 클릭시
            btnRegister.setOnClickListener {
                val loginIntent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(loginIntent)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // 사용자 ID를 SharedPreferences에 저장하는 메서드
    private fun saveUserIdToPreferences(userId: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()  // 사용자 ID 저장
    }
}