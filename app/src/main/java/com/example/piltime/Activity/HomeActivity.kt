package com.example.piltime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.piltime.Activity.AlarmSystemActivity
import com.example.piltime.Activity.CommunityActivity

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

        // 버튼 초기화
        val medicineButton = findViewById<Button>(R.id.medicineButton)
        val scheduleButton = findViewById<Button>(R.id.scheduleButton)
        val historyButton = findViewById<Button>(R.id.historyButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val communityButton = findViewById<Button>(R.id.communityButton) // 커뮤니티 버튼 초기화
        val alarmButton = findViewById<Button>(R.id.alarmButton)

        // 알람 관리 버튼 클릭 리스너
        alarmButton.setOnClickListener {
            // TODO: 알람 관리 화면으로 이동
            Toast.makeText(this, "알람 관리 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            // 알람 관리 화면으로 이동하는 Intent 추가
            val intent = Intent(this, AlarmSystemActivity::class.java)
            startActivity(intent)

        }

        // 약 관리 버튼
        medicineButton.setOnClickListener {
            if (isGuest) {
                showGuestLimitMessage()
            } else {
                // TODO: 약 관리 화면으로 이동
                Toast.makeText(this, "약 관리 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 일정 관리 버튼
        scheduleButton.setOnClickListener {
            if (isGuest) {
                showGuestLimitMessage()
            } else {
                // TODO: 일정 관리 화면으로 이동
                Toast.makeText(this, "일정 관리 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 복용 기록 버튼
        historyButton.setOnClickListener {
            if (isGuest) {
                showGuestLimitMessage()
            } else {
                // TODO: 복용 기록 화면으로 이동
                Toast.makeText(this, "복용 기록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 커뮤니티 버튼 클릭 리스너
        communityButton.setOnClickListener {
                // TODO: 커뮤니티 화면으로 이동
                Toast.makeText(this, "커뮤니티 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
                // Intent로 커뮤니티 화면으로 이동하는 코드 추가
                val intent = Intent(this, CommunityActivity::class.java)
                startActivity(intent)
        }

        // 로그아웃 버튼
        logoutButton.setOnClickListener {
            Toast.makeText(this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            finish() // MainActivity로 돌아가기
        }

        // 게스트 사용자일 경우 버튼 스타일 변경
        if (isGuest) {
            medicineButton.alpha = 0.5f
            scheduleButton.alpha = 0.5f
            historyButton.alpha = 0.5f
            communityButton.alpha = 0.5f // 커뮤니티 버튼도 비활성화
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
