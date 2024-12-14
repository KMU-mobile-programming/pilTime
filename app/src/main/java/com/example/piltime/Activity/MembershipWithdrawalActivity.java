package com.example.piltime.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medtime.UserDBHelper;
import com.example.piltime.R;

public class MembershipWithdrawalActivity extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPassword;
    private Button buttonConfirmWithdrawal;
    private ImageButton buttonCancel;

    UserDBHelper DB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_withdrawal);

        // UI 요소 연결
        editTextId = findViewById(R.id.editTextTextMultiLine);
        editTextPassword = findViewById(R.id.editTextTextPassword2);
        buttonConfirmWithdrawal = findViewById(R.id.button_confirm_withdrawal);
        buttonCancel = findViewById(R.id.button_cancel);

        DB = new UserDBHelper(this);
        // 사용자 ID 설정 (예: 현재 로그인한 사용자 ID)
        String userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            userId = "Unknown";
        }
        String userPassword = getIntent().getStringExtra("USER_PASSWORD");
        boolean isGuest = getIntent().getBooleanExtra("IS_GUEST", false);

        // 뒤로가기 버튼 동작
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 화면 종료
            }
        });

        // 회원탈퇴 버튼 동작
        String finalUserId = userId;
        buttonConfirmWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저 아이디와 비밀번호 - 실제로는 서버나 로컬에서 불러올 값을 가정
                final String realUserId = finalUserId;
                final String realPassword = userPassword;

                String userId = editTextId.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // 입력값 검증
                if (TextUtils.isEmpty(userId)) {
                    showToast("아이디를 입력해주세요.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    showToast("비밀번호를 입력해주세요.");
                    return;
                }

                if (!userId.equals(realUserId)) {
                    showToast("아이디가 일치하지 않습니다.");
                    return;
                }

                if(!password.equals(realPassword)) {
                    showToast("비밀번호가 일치하지 않습니다.");
                    return;
                }

                // 회원탈퇴 확인 대화상자 표시
                showConfirmationDialog(userId, password);

            }
        });
    }



    // 회원탈퇴 확인 대화상자
    private void showConfirmationDialog(String userId, String password) {
        new AlertDialog.Builder(this)
                .setTitle("회원탈퇴")
                .setMessage("정말로 회원탈퇴를 진행하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 탈퇴 로직 실행 (예: 서버 요청)
                        processWithdrawal(userId, password);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    // 회원탈퇴 처리 로직
    private void processWithdrawal(String userId, String password) {
        if (DB.checkUserpass(userId, password)) {
            DB.deleteUser(userId, password);
            showToast("회원탈퇴가 완료되었습니다.");
            finish(); // 회원탈퇴 완료 후 화면 종료
        } else {
            showToast("회원탈퇴에 실패했습니다. 다시 시도해주세요.");
        }
    }

    // Toast 메시지 표시
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
