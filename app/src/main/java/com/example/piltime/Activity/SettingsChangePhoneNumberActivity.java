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

import com.example.medtime.UserDBHelper;
import com.example.piltime.R;

public class SettingsChangePhoneNumberActivity extends AppCompatActivity {

    private static UserDBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_change_phone_number);

        // XML 요소 연결
        ImageButton buttonCancel = findViewById(R.id.button_cancel);
        EditText editTextNewPhone = findViewById(R.id.editTextNewPhone);
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        Button buttonConfirmPhoneNumber = findViewById(R.id.button_confirm_phone_number);

        DB = new UserDBHelper(this);
        // 사용자 ID 설정 (예: 현재 로그인한 사용자 ID)
        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "Unknown";
        }
        boolean isGuest = getIntent().getBooleanExtra("IS_GUEST", false);

        // 휴대폰 번호 변경 버튼 동작 설정
        String finalUserId = userId;
        buttonConfirmPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhone = editTextNewPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // 입력값 유효성 검사
                if (TextUtils.isEmpty(newPhone)) {
                    Toast.makeText(SettingsChangePhoneNumberActivity.this, "새 휴대폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SettingsChangePhoneNumberActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 확인
                if (DB.checkUserpass(finalUserId, password)) {
                    // 휴대폰 번호 변경
                    boolean isUpdated = DB.updatePhoneNumber(finalUserId, newPhone);
                    if (isUpdated) {
                        showAlertDialog("휴대폰 번호 변경 성공", "휴대폰 번호가 성공적으로 변경되었습니다.");
                        finish();
                    } else {
                        showAlertDialog("휴대폰 번호 변경 실패", "휴대폰 번호 변경 중 오류가 발생했습니다.");
                    }
                } else {
                    showAlertDialog("휴대폰 번호 변경 실패", "비밀번호가 올바르지 않습니다.");
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 필드 초기화
                editTextNewPhone.setText("");
                editTextPassword.setText("");
                finish();
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show();
    }
}
