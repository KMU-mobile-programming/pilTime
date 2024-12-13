package com.example.piltime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.piltime.Activity.AlarmSystemActivity
import com.example.piltime.Activity.CommunityActivity
import com.example.piltime.Activity.ProfileActivity
import com.example.piltime.Activity.SettingAlarmActivity
import com.example.piltime.Activity.SettingsActivity // 설정 액티비티 가져오기
import com.example.piltime.Activity.SettingsAlarmSettingActivity
import com.example.piltime.Database.DataBase
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    // 뒤로가기 관련 변수 추가
    private var backPressedTime: Long = 0
    private val BACK_PRESS_INTERVAL: Long = 1300 // 1.3초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Intent로부터 사용자 정보 받기
        val userId = intent.getStringExtra("USER_ID") ?: "Unknown"
        val isGuest = intent.getBooleanExtra("IS_GUEST", false)

        // 환영 메시지 설정

        val welcomeTextView = findViewById<TextView>(R.id.welcomeText)
        val welcomeMessage = if (isGuest) {
            "게스트로 접속하셨습니다\n일부 기능이 제한됩니다"
        } else {
            val dbHelper = DataBase(this)  // DataBase 인스턴스 생성
            val userNick = dbHelper.getNickById(userId)  // 인스턴스 메서드로 호출
            "${userNick}님 환영합니다"
        }
        welcomeTextView.text = welcomeMessage

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton) // 설정 버튼 초기
        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnCommunity = findViewById<Button>(R.id.btnCommunity)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnAlarmSettings = findViewById<Button>(R.id.btnAlarmSettings)

        // 홈 버튼 (현재 화면이므로 클릭 시 토스트 메시지)
        btnHome.setOnClickListener {
            Toast.makeText(this, "현재 홈 화면입니다.", Toast.LENGTH_SHORT).show()
        }

        // 커뮤니티 버튼
        btnCommunity.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            intent.putExtra("userId", userId) // userId 전달
            intent.putExtra("isGuest", isGuest) // isGuest 여부 전달
            startActivity(intent)
        }

        // 프로필 버튼
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", userId) // userId 전달
            intent.putExtra("isGuest", isGuest) // isGuest 여부 전달
            startActivity(intent)
        }

        // 알람 설정 버튼
        btnAlarmSettings.setOnClickListener {
            val intent = Intent(this, AlarmSystemActivity::class.java)
            intent.putExtra("userId", userId) // userId 전달
            intent.putExtra("isGuest", isGuest) // isGuest 여부 전달
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            // 설정 화면으로 이동
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("userId", userId) // userId 전달
            intent.putExtra("isGuest", isGuest) // isGuest 여부 전달
            startActivity(intent)
        }


        // 로그아웃 버튼
        logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()  // 모든 로그인 정보 삭제
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            finish() // MainActivity로 돌아가기
        }

        // 게스트 사용자일 경우 버튼 스타일 변경
        if (isGuest) {

        }
    }

    private fun showGuestLimitMessage() {
        Toast.makeText(
            this,
            "게스트는 이용할 수 없는 기능입니다.\n회원가입 후 이용해주세요.",
            Toast.LENGTH_SHORT
        ).show()
    }
    // 뒤로가기 처리를 위한 메서드 추가
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
