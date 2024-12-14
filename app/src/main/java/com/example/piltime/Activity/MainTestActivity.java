/*
package com.example.piltime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.Database.AppDatabaseHelper;
import com.example.piltime.Database.DatabaseManager;
import com.example.piltime.R;

import java.util.List;

public class MainTestActivity extends AppCompatActivity {

    private DatabaseManager databaseManager; // DatabaseManager 객체
    private String userId = "1";       // 예제 사용자 ID (고정값으로 설정)
    private String userName = "testuser";                // 사용자 이름

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintest);

        Button goToCommunityButton = findViewById(R.id.goToCommunityButton);
        Button addMedicineButton = findViewById(R.id.addMedicineButton);
        Button viewMedicinesButton = findViewById(R.id.viewMedicinesButton);
        Button clearDatabaseButton = findViewById(R.id.clearDatabaseButton); // 데이터베이스 초기화 버튼

        // DatabaseManager 초기화
        databaseManager = new AppDatabaseHelper(this);

        // 유저 정보 초기화 (유저 이름을 설정하지 않았으면 다이얼로그로 받기)
        if (!databaseManager.userExists(userId)) {
            showUserNameDialog();
        } else {
            userName = databaseManager.getUserName(userId);
        }

        // 유저 정보 CommunityActivity로 전송
        goToCommunityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainTestActivity.this, CommunityActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // 복용약 추가 버튼 클릭 시
        addMedicineButton.setOnClickListener(v -> {
            // 사용자로부터 약 이름을 입력받는 다이얼로그 표시
            showAddMedicineDialog();
        });

        // 복용약 리스트 보기 버튼 클릭 시
        viewMedicinesButton.setOnClickListener(v -> {
            LinearLayout medicineListLayout = findViewById(R.id.medicineListLayout);
            medicineListLayout.removeAllViews(); // 기존 뷰를 모두 제거

            // 데이터베이스에서 복용약 리스트 불러오기
            List<String> medicineList = databaseManager.getMedicinesByUserId(userId);

            for (String medicine : medicineList) {
                LinearLayout medicineLayout = new LinearLayout(MainTestActivity.this);
                medicineLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView medicineTextView = new TextView(MainTestActivity.this);
                medicineTextView.setText(medicine);
                medicineTextView.setPadding(10, 10, 10, 10);

                // 삭제 버튼
                Button deleteButton = new Button(MainTestActivity.this);
                deleteButton.setText("삭제");
                deleteButton.setOnClickListener(v1 -> {
                    // 복용약 삭제
                    ((AppDatabaseHelper) databaseManager).removeMedicine(userId, medicine);
                    Toast.makeText(MainTestActivity.this, medicine + "이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    viewMedicinesButton.performClick(); // 리스트 새로고침
                });

                // 복용약 이름과 삭제 버튼을 하나의 레이아웃에 배치
                medicineLayout.addView(medicineTextView);
                medicineLayout.addView(deleteButton);

                medicineListLayout.addView(medicineLayout);
            }
        });

        // 데이터베이스 초기화 버튼 클릭 시
        clearDatabaseButton.setOnClickListener(v -> {
            // 모든 복용약 데이터 삭제
            ((AppDatabaseHelper) databaseManager).clearAllMedicines(userId);
            Toast.makeText(MainTestActivity.this, "모든 복용약 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    // 유저 이름 입력받는 다이얼로그
    private void showUserNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("유저 정보 입력");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText userNameInput = new EditText(this);
        userNameInput.setHint("유저 이름 입력");
        layout.addView(userNameInput);

        builder.setView(layout);

        builder.setPositiveButton("확인", (dialog, which) -> {
            userName = userNameInput.getText().toString();
            if (!userName.isEmpty()) {
                databaseManager.addUser(userId, userName, null);
                Toast.makeText(this, "유저 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "유저 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                showUserNameDialog();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    // 복용약 추가 다이얼로그
    private void showAddMedicineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("복용약 추가");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText medicineNameInput = new EditText(this);
        medicineNameInput.setHint("약 이름 입력");
        layout.addView(medicineNameInput);

        EditText dosageInput = new EditText(this);
        dosageInput.setHint("용량 입력");
        layout.addView(dosageInput);

        EditText timeInput = new EditText(this);
        timeInput.setHint("복용 시간 입력");
        layout.addView(timeInput);

        EditText categoryInput = new EditText(this);
        categoryInput.setHint("약 종류 입력 (영양제, 처방약 등)");
        layout.addView(categoryInput);

        builder.setView(layout);

        builder.setPositiveButton("추가", (dialog, which) -> {
            String medicineName = medicineNameInput.getText().toString();
            String dosage = dosageInput.getText().toString();
            String time = timeInput.getText().toString();
            String category = categoryInput.getText().toString();

            if (!medicineName.isEmpty() && !dosage.isEmpty() && !time.isEmpty() && !category.isEmpty()) {
                // 복용약을 데이터베이스에 추가
                databaseManager.addMedicine(userId, medicineName, dosage, time, category, 1.0f);
                Toast.makeText(MainTestActivity.this, "복용약이 추가되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainTestActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", null);

        builder.show();
    }
}
*/