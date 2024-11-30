package com.example.piltime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piltime.Activity.AlarmSystemActivity
import com.example.piltime.Activity.CommunityActivity
import com.example.piltime.Activity.ProfileActivity
import com.example.piltime.Activity.SettingsActivity // 설정 액티비티 가져오기
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
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
            "${userId}님 환영합니다"
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
            startActivity(intent)
        }

        // 프로필 버튼
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // 알람 설정 버튼
        btnAlarmSettings.setOnClickListener {
            val intent = Intent(this, AlarmSystemActivity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener {
            // 설정 화면으로 이동
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        // 로그아웃 버튼
        logoutButton.setOnClickListener {
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
}
