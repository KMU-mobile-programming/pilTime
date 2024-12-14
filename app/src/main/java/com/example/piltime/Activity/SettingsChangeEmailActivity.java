package com.example.piltime.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.R;

public class SettingsChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_email);

        // XML 요소 연결
        ImageButton buttonCancel = findViewById(R.id.button_cancel);
        EditText editTextNewEmail = findViewById(R.id.editTextTextEmailAddress);
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        Button buttonConfirmEmail = findViewById(R.id.button_confirm_email);

        // 현재 이메일 - 실제로는 서버나 로컬에서 불러올 값을 가정
        final String currentEmail = "user@example.com";

        // 이메일 변경 버튼 동작 설정
        buttonConfirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = editTextNewEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // 입력값 유효성 검사
                if (TextUtils.isEmpty(newEmail)) {
                    Toast.makeText(SettingsChangeEmailActivity.this, "새 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SettingsChangeEmailActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newEmail.equals(currentEmail)) {
                    showAlertDialog("이메일 변경", "사용 중인 이메일과 다른 이메일을 입력해주세요.");
                    return;
                }

                // 서버 API를 통한 이메일 변경 요청 (임시 구현)
                changeEmail(newEmail, password);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 필드 초기화
                editTextNewEmail.setText("");
                editTextPassword.setText("");
                finish();
            }
        });
    }

    private void changeEmail(String newEmail, String password) {
        // 여기서는 간단히 성공/실패를 가정한 코드를 작성합니다.
        // 실제로는 서버 API 호출이 필요합니다.
        boolean isPasswordCorrect = "password123".equals(password); // 임시 비밀번호 확인 로직

        if (isPasswordCorrect) {
            // 이메일 변경 성공
            showAlertDialog("이메일 변경 성공", "이메일이 성공적으로 변경되었습니다.");
            finish();
        } else {
            // 비밀번호 불일치
            showAlertDialog("이메일 변경 실패", "비밀번호가 올바르지 않습니다.");
        }
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show();
    }
}
