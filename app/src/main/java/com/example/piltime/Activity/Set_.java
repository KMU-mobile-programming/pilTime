package com.example.piltime.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;

public class Set_ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);

        // XML 요소 연결
        Button buttonSave = findViewById(R.id.button_save);
        Switch switchAllAlarm = findViewById(R.id.switch_all_alarm);
        Switch switchStockAlarm = findViewById(R.id.switch_stock_alarm);
        Switch switchCommentAlarm = findViewById(R.id.switch_comment_alarm);
        Switch switchCommentAlarm2 = findViewById(R.id.switch_comment_alarm2);
        Switch switchLikeAlarm = findViewById(R.id.switch_like_alarm);
        Switch switchCommunityAlarm = findViewById(R.id.switch_community_alarm);

        // 전체 알림 ON/OFF 스위치 동작
        switchAllAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 전체 알림 설정 변경 시, 다른 스위치 비활성화/활성화
            if (!isChecked) {
                switchStockAlarm.setChecked(false);
                switchCommunityAlarm.setChecked(false);
            }
            switchStockAlarm.setEnabled(isChecked);
            switchCommentAlarm.setEnabled(isChecked);
            switchCommentAlarm2.setEnabled(isChecked);
            switchLikeAlarm.setEnabled(isChecked);
            switchCommunityAlarm.setEnabled(isChecked);
            Toast.makeText(this, isChecked ? "전체 알림 ON" : "전체 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        // 커뮤니티 알림 스위치 동작
        switchCommunityAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                // 커뮤니티 알림이 OFF가 되면, 댓글 알림, 대댓글 알림, 좋아요 알림을 OFF로 설정
                switchCommentAlarm.setChecked(false);
                switchCommentAlarm2.setChecked(false);
                switchLikeAlarm.setChecked(false);
                Toast.makeText(this, "커뮤니티 알림 OFF: 관련 알림이 모두 비활성화되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "커뮤니티 알림 ON", Toast.LENGTH_SHORT).show();
            }
        });

        // 개별 알림 스위치 동작
        switchStockAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "재고 알림 ON" : "재고 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchCommentAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "댓글 알림 ON" : "댓글 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchCommentAlarm2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "대댓글 알림 ON" : "대댓글 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchLikeAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "좋아요 알림 ON" : "좋아요 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        // 저장
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 필드 초기화
                finish();
            }
        });
    }
}
