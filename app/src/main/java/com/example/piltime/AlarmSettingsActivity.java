package com.example.piltime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmSettingsActivity extends AppCompatActivity {

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
        EditText editTextStockThreshold1 = findViewById(R.id.editTextNumber);
        EditText editTextStockThreshold2 = findViewById(R.id.editTextNumber2);

        // 전체 알림 ON/OFF 스위치 동작
        switchAllAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 전체 알림 설정 변경 시, 다른 스위치 비활성화/활성화
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

        // 재고 알림 기준 입력값 확인 및 저장
        buttonSave.setOnClickListener(v -> {
            String stockThreshold1 = editTextStockThreshold1.getText().toString().trim();
            String stockThreshold2 = editTextStockThreshold2.getText().toString().trim();

            if (stockThreshold1.isEmpty() || stockThreshold2.isEmpty()) {
                Toast.makeText(this, "재고 알림 기준값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int threshold1 = Integer.parseInt(stockThreshold1);
                int threshold2 = Integer.parseInt(stockThreshold2);

                if (threshold1 < 0 || threshold2 < 0) {
                    Toast.makeText(this, "재고 알림 기준값은 0 이상의 숫자여야 합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 저장 로직 (SharedPreferences 또는 서버 API 호출 가능)
                    saveThresholds(threshold1, threshold2);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "유효한 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 재고 알림 기준값 저장 메서드
    private void saveThresholds(int threshold1, int threshold2) {
        // 예: SharedPreferences 사용
        Toast.makeText(this, "재고 알림 기준값이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
