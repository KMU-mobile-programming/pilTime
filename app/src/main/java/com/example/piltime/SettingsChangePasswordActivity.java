package com.example.piltime;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivitySettingsChangePassword", "Activity Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_password);
        Log.d("ActivitySettingsChangePassword", "after setcontentview");

        // XML 요소 연결
        TextView textViewChangePassword = findViewById(R.id.textView_change_password);
        TextView textViewPasswordGuide = findViewById(R.id.textView_password_guide);
        EditText editTextNewPassword = findViewById(R.id.editTextTextPassword_new_password);
        EditText editTextCheckNewPassword = findViewById(R.id.editTextTextPassword_check_new_password);
        EditText editTextCurrentPassword = findViewById(R.id.editTextTextPassword_current_password);
        Button buttonConfirmPassword = findViewById(R.id.button_confirm_password);
        Button buttonCancel = findViewById(R.id.button_save);

        // 비밀번호 변경 버튼 동작 설정
        buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 비밀번호 - 실제로는 서버나 로컬에서 불러올 값을 가정
                final String realCurrentPassword = "password123";

                String currentPassword = editTextCurrentPassword.getText().toString().trim();
                String newPassword = editTextNewPassword.getText().toString().trim();
                String checkNewPassword = editTextCheckNewPassword.getText().toString().trim();

                // 입력값 확인
                if (TextUtils.isEmpty(currentPassword)) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "현재 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(checkNewPassword)) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "새 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(checkNewPassword)) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.length() < 8 || newPassword.length() > 20) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "비밀번호는 8~20자 사이여야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 유효성 검사 (예: 영문, 숫자, 특수문자 조합 확인)
                if (!isValidPassword(newPassword)) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "비밀번호는 영문, 숫자, 특수문자 2종류 이상 조합이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 검사
                if (newPassword.equals(realCurrentPassword)) {
                    Toast.makeText(SettingsChangePasswordActivity.this, "사용 중인 비밀번호와 다른 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (realCurrentPassword.equals(currentPassword)) {
                    // 비밀번호 변경 성공
                    Toast.makeText(SettingsChangePasswordActivity.this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(SettingsChangePasswordActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 취소 버튼 동작 설정
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 필드 초기화
                editTextCurrentPassword.setText("");
                editTextNewPassword.setText("");
                editTextCheckNewPassword.setText("");
                finish();
            }
        });
    }

    // 비밀번호 유효성 검사 함수
    private boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }

        int count = 0;
        if (hasLetter) count++;
        if (hasDigit) count++;
        if (hasSpecialChar) count++;

        return count >= 2;
    }
}
