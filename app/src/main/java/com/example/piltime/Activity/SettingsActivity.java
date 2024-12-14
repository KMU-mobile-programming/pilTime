package com.example.piltime.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.MainActivity;
import com.example.piltime.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView textViewSettings;
    private TextView textViewAccount;
    private Button buttonCancel, buttonId, buttonPassword, buttonEmail;
    private TextView textViewApplicationSettings;
    private Button buttonAlarmSettings;
    private TextView textViewEtc;
    private Button buttonLogout, buttonMembershipWithdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivitySettings", "Activity Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // XML 파일 이름에 맞게 변경하세요.
        Log.d("ActivitySettings", "after setcontentview");


        // View 초기화
        buttonCancel = findViewById(R.id.button_save);
        textViewSettings = findViewById(R.id.textView_settings);
        textViewAccount = findViewById(R.id.textView_account);
        buttonId = findViewById(R.id.button_id);
        buttonPassword = findViewById(R.id.button_password);
        buttonEmail = findViewById(R.id.button_email);
        textViewApplicationSettings = findViewById(R.id.textView_application_settings);
        buttonAlarmSettings = findViewById(R.id.button_alarm_settings);
        textViewEtc = findViewById(R.id.textView_etc);
        buttonLogout = findViewById(R.id.button_logout);
        buttonMembershipWithdrawal = findViewById(R.id.button_membership_withdrawal);

        // 버튼에 클릭 리스너 추가
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로 가기 버튼 클릭 시 수행할 동작 - 프로필로 돌아가기
            }
        });

        buttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 버튼 클릭 시 수행할 동작
            }
        });

        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 변경 버튼 클릭 시 수행할 동작
                Intent intent = new Intent(getApplicationContext(), com.example.piltime.Activity.SettingsChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 변경 버튼 클릭 시 수행할 동작
                Intent intent = new Intent(getApplicationContext(), com.example.piltime.Activity.SettingsChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        buttonAlarmSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 알림 설정 버튼 클릭 시 수행할 동작
                Intent intent = new Intent(getApplicationContext(), com.example.piltime.Activity.SettingsAlarmSettingActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences에서 로그인 정보 삭제
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // 모든 로그인 정보 삭제
                editor.apply(); // 변경사항을 적용

                // MainActivity로 이동
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // 로그아웃 알림 메시지
                Toast.makeText(SettingsActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                // 현재 Activity 종료 (선택적)
                finish();
            }
        });

        buttonMembershipWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원탈퇴 버튼 클릭 시 수행할 동작
                Intent intent = new Intent(getApplicationContext(), MembershipWithdrawalActivity.class);
                startActivity(intent);
            }
        });
    }
}
