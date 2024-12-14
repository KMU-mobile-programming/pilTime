package com.example.piltime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piltime.Database.DataBase

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var editTextId: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnGuestLogin: Button
    private lateinit var checkBoxAutoLogin: CheckBox
    private var DB: DataBase? = null
    private var backPressedTime: Long = 0
    private val BACK_PRESS_INTERVAL: Long = 1300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            DB = DataBase(this)

            // View 초기화
            btnLogin = findViewById(R.id.btnLogin)
            editTextId = findViewById(R.id.editTextId)
            editTextPassword = findViewById(R.id.editTextPassword)
            btnRegister = findViewById(R.id.btnRegister)
            btnGuestLogin = findViewById(R.id.btnGuestLogin)
            checkBoxAutoLogin = findViewById(R.id.checkBoxAutoLogin)

            // 자동 로그인 확인
            checkAutoLogin()

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
                        // 로그인 유지 체크됐을 경우 정보 저장
                        if (checkBoxAutoLogin.isChecked) {
                            saveLoginInfo(user, pass)
                        }

                        val intent = Intent(this, HomeActivity::class.java).apply {
                            putExtra("USER_ID", user)
                            putExtra("IS_GUEST", false)
                        }
                        Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // 게스트 로그인 버튼 클릭
            btnGuestLogin.setOnClickListener {
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("USER_ID", "guest")
                        putExtra("IS_GUEST", true)
                    }
                    Toast.makeText(this, "게스트로 로그인되었습니다.", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
            }

            // 회원가입 버튼 클릭시
            btnRegister.setOnClickListener {
                val loginIntent = Intent(this, RegisterActivity::class.java)
                startActivity(loginIntent)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserIdToPreferences(userId: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    // 로그인 정보 저장
    private fun saveLoginInfo(userId: String, password: String) {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.putString("password", password)
        editor.putBoolean("autoLogin", true)
        editor.apply()
    }

    // 자동 로그인 확인
    private fun checkAutoLogin() {
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val autoLogin = sharedPreferences.getBoolean("autoLogin", false)

        if (autoLogin) {
            val userId = sharedPreferences.getString("userId", "") ?: ""
            val password = sharedPreferences.getString("password", "") ?: ""

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val checkUserpass = DB?.checkUserpass(userId, password) ?: false
                if (checkUserpass) {
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("USER_ID", userId)
                        putExtra("IS_GUEST", false)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < BACK_PRESS_INTERVAL) {
            super.onBackPressed()
            finishAffinity() // 모든 액티비티 종료
            System.exit(0)   // 앱 프로세스 종료
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}