package com.example.piltime;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsAlarmSettingActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private static final String PREFS_NAME = "AlarmSettings";
    private static final String KEY_ALL_ALARM = "all_alarm";
    private static final String KEY_STOCK_ALARM = "stock_alarm";
    private static final String KEY_COMMENT_ALARM = "comment_alarm";
    private static final String KEY_COMMENT_ALARM2 = "comment_alarm2";
    private static final String KEY_LIKE_ALARM = "like_alarm";
    private static final String KEY_COMMUNITY_ALARM = "community_alarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);

        // SharedPreferences 초기화
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // XML 요소 연결
        Button buttonSave = findViewById(R.id.button_save);
        Switch switchAllAlarm = findViewById(R.id.switch_all_alarm);
        Switch switchStockAlarm = findViewById(R.id.switch_stock_alarm);
        Switch switchCommentAlarm = findViewById(R.id.switch_comment_alarm);
        Switch switchCommentAlarm2 = findViewById(R.id.switch_comment_alarm2);
        Switch switchLikeAlarm = findViewById(R.id.switch_like_alarm);
        Switch switchCommunityAlarm = findViewById(R.id.switch_community_alarm);

        // SharedPreferences에서 스위치 상태 로드
        switchAllAlarm.setChecked(preferences.getBoolean(KEY_ALL_ALARM, true));
        switchStockAlarm.setChecked(preferences.getBoolean(KEY_STOCK_ALARM, true));
        switchCommentAlarm.setChecked(preferences.getBoolean(KEY_COMMENT_ALARM, true));
        switchCommentAlarm2.setChecked(preferences.getBoolean(KEY_COMMENT_ALARM2, true));
        switchLikeAlarm.setChecked(preferences.getBoolean(KEY_LIKE_ALARM, true));
        switchCommunityAlarm.setChecked(preferences.getBoolean(KEY_COMMUNITY_ALARM, true));

        // 실제 알람 ON/OFF 기능 개발중
        // AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 전체 알림 ON/OFF 스위치 동작
        switchAllAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 전체 알림 설정 변경 시, 다른 스위치 비활성화/활성화
            if (!isChecked) {
                switchStockAlarm.setChecked(false);
                switchCommunityAlarm.setChecked(false);
                savePreference(KEY_ALL_ALARM, false);
                savePreference(KEY_COMMUNITY_ALARM, false);
                // 실제 알람 ON/OFF 기능 개발중
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    alarmManager.cancelAll();
                }
                 */
            }
            else {
                switchStockAlarm.setChecked(true);
                switchCommunityAlarm.setChecked(true);
                savePreference(KEY_ALL_ALARM, true);
                savePreference(KEY_COMMUNITY_ALARM, true);
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
                savePreference(KEY_COMMUNITY_ALARM, false);
                savePreference(KEY_COMMENT_ALARM, false);
                savePreference(KEY_COMMENT_ALARM2, false);
                savePreference(KEY_LIKE_ALARM, false);
                Toast.makeText(this, "커뮤니티 알림 OFF: 관련 알림이 모두 비활성화되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                switchCommentAlarm.setChecked(true);
                switchCommentAlarm2.setChecked(true);
                switchLikeAlarm.setChecked(true);
                savePreference(KEY_COMMUNITY_ALARM, true);
                savePreference(KEY_COMMENT_ALARM, true);
                savePreference(KEY_COMMENT_ALARM2, true);
                savePreference(KEY_LIKE_ALARM, true);
                Toast.makeText(this, "커뮤니티 알림 ON", Toast.LENGTH_SHORT).show();
            }
        });

        // 개별 알림 스위치 동작
        switchStockAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference(KEY_STOCK_ALARM, isChecked);
            Toast.makeText(this, isChecked ? "재고 알림 ON" : "재고 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchCommentAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference(KEY_COMMENT_ALARM, isChecked);
            Toast.makeText(this, isChecked ? "댓글 알림 ON" : "댓글 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchCommentAlarm2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference(KEY_COMMENT_ALARM2, isChecked);
            Toast.makeText(this, isChecked ? "대댓글 알림 ON" : "대댓글 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        switchLikeAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePreference(KEY_LIKE_ALARM, isChecked);
            Toast.makeText(this, isChecked ? "좋아요 알림 ON" : "좋아요 알림 OFF", Toast.LENGTH_SHORT).show();
        });

        // 저장 버튼 동작
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsAlarmSettingActivity.this, "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
