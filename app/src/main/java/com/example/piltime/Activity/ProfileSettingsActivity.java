package com.example.piltime.Activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piltime.Database.DataBase;
import com.example.piltime.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    boolean ispersonalprivate = true;
    boolean amiboy = true;
    String currentNick;
    String userId; // userId를 여기서 선언

    int profilepic = R.drawable.ic_launcher_foreground;
    DataBase dbHelper;
    TextView profileSetNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        // userId 초기화
        userId = getIntent().getStringExtra("userId");

        ImageView profileSetPic = (ImageView) findViewById(R.id.profileSetPic);
        profileSetPic.setImageDrawable(getDrawable(profilepic));

        dbHelper = new DataBase(this);

        // 닉네임 표시를 위한 TextView 초기화
        profileSetNick = (TextView) findViewById(R.id.profileSetNick); // 닉네임 TextView 초기화

        // 닉네임 조회
        loadUserProfile();

        // 닉네임 수정 버튼 클릭 리스너
        findViewById(R.id.editNickButton).setOnClickListener(v -> editNickname());

        // 성별 선택 및 개인 정보 설정 관련 코드...
        LinearLayout profileSetMaleB = (LinearLayout) findViewById(R.id.profileSetMaleB);
        LinearLayout profileSetFemaleB = (LinearLayout) findViewById(R.id.profileSetFemaleB);
        TextView profileSetMale = (TextView) findViewById(R.id.profileSetMale);
        TextView profileSetFemale = (TextView) findViewById(R.id.profileSetFemale);
        if (amiboy) {
            profileSetMaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
            profileSetFemaleB.setBackground(null);
            profileSetMale.setTextColor(0xFFFFFFFF);
            profileSetFemale.setTextColor(0xFF000000);
        } else {
            profileSetMaleB.setBackground(null);
            profileSetFemaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
            profileSetMale.setTextColor(0xFF000000);
            profileSetFemale.setTextColor(0xFFFFFFFF);
        }

        LinearLayout personalB = (LinearLayout) findViewById(R.id.personalB);
        ImageView personalPrivateS = (ImageView) findViewById(R.id.personalPrivateS);
        TextView personalOnOff = (TextView) findViewById(R.id.personalOnOff);
        if (ispersonalprivate) {
            personalB.setGravity(Gravity.LEFT);
            personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_984f4f));
            personalOnOff.setText("OFF");
            personalOnOff.setTextColor(0xFF984F4F);
        } else {
            personalB.setGravity(Gravity.RIGHT);
            personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_f3d1a5));
            personalOnOff.setText("ON");
            personalOnOff.setTextColor(0xFFF07070);
        }

        LinearLayout personalPrivate = (LinearLayout) findViewById(R.id.personalPrivate);
        personalPrivate.setOnClickListener(v -> {
            ispersonalprivate = !ispersonalprivate;
            if (ispersonalprivate) {
                personalB.setGravity(Gravity.LEFT);
                personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_984f4f));
                personalOnOff.setText("OFF");
                personalOnOff.setTextColor(0xFF984F4F);
            } else {
                personalB.setGravity(Gravity.RIGHT);
                personalPrivateS.setImageDrawable(getDrawable(R.drawable.rounded_f3d1a5));
                personalOnOff.setText("ON");
                personalOnOff.setTextColor(0xFFF07070);
            }
        });

        LinearLayout profileSetSex = (LinearLayout) findViewById(R.id.profileSetSex);
        profileSetSex.setOnClickListener(v -> {
            amiboy = !amiboy;
            if (amiboy) {
                profileSetMaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
                profileSetFemaleB.setBackground(null);
                profileSetMale.setTextColor(0xFFFFFFFF);
                profileSetFemale.setTextColor(0xFF000000);
            } else {
                profileSetMaleB.setBackground(null);
                profileSetFemaleB.setBackground(getDrawable(R.drawable.rounded_984f4f));
                profileSetMale.setTextColor(0xFF000000);
                profileSetFemale.setTextColor(0xFFFFFFFF);
            }
        });
    }

    private void loadUserProfile() {
        // DB에서 사용자 정보를 조회
        currentNick = dbHelper.getNickById(userId); // getNickById는 데이터베이스에서 닉네임을 가져오는 메서드
        profileSetNick.setText(currentNick); // 닉네임 표시
    }

    private void editNickname() {
        // EditText를 사용하여 새로운 닉네임을 입력받는 부분
        EditText editNickEditText = new EditText(this);
        editNickEditText.setText(currentNick); // 기존 닉네임 미리 채우기
        editNickEditText.setHint("새 닉네임 입력"); // 힌트 추가
        editNickEditText.setPadding(16, 16, 16, 16); // 패딩 추가
        editNickEditText.setBackgroundResource(R.drawable.edittext_background); // 배경 리소스 설정
        editNickEditText.setTextColor(0xFF000000); // 텍스트 색상 설정
        editNickEditText.setHintTextColor(0xFF999999); // 힌트 색상 설정

        // 레이아웃 설정
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32); // 레이아웃 패딩
        layout.setBackgroundColor(0xFFFFFFFF); // 배경색 흰색으로 설정
        layout.setGravity(Gravity.CENTER); // 자식 뷰를 가운데 정렬

        // EditText와 버튼의 마진 설정
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(0, 0, 0, 16); // EditText와 버튼 간의 간격 추가
        editNickEditText.setLayoutParams(editTextParams); // 레이아웃 파라미터 설정

        // 버튼 클릭 시 닉네임 수정 및 DB 업데이트
        Button confirmButton = new Button(this);
        confirmButton.setText("Confirm");
        confirmButton.setPadding(16, 16, 16, 16); // 버튼 패딩
        confirmButton.setBackgroundResource(R.drawable.button_background); // 버튼 배경 리소스 설정
        confirmButton.setTextColor(0xFFFFFFFF); // 버튼 텍스트 색상
        confirmButton.setGravity(Gravity.CENTER); // 버튼 텍스트 가운데 정렬

        // 버튼 클릭 리스너 설정
        confirmButton.setOnClickListener(v -> {
            String newNick = editNickEditText.getText().toString();
            if (!newNick.isEmpty()) {
                dbHelper.updateNickById(userId, newNick); // DB에서 닉네임 업데이트
                profileSetNick.setText(newNick); // TextView 업데이트
                currentNick = newNick; // 현재 닉네임 갱신
            }
            // 원래 레이아웃으로 복귀
            setContentView(R.layout.activity_profilesettings);
            loadUserProfile(); // 사용자 정보 다시 로드
        });

        // 레이아웃에 EditText와 버튼 추가
        layout.addView(editNickEditText);
        layout.addView(confirmButton);

        // 원래 레이아웃으로 복귀하기 전에 레이아웃 설정
        setContentView(layout);
    }


}
