package com.example.piltime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

//해야 하는 거: 요일은 됨. 이제 날짜 혹은 매일 알람으로 모드 바꾸는 것과 UI 변경, 세팅 변경 및 설정
public class SettingAlarmActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;

    private Button alarmButtonBasic;
    private Button addAlarmButton;
    private ImageButton saveButton;

    private LinearLayout layoutAlarms;
    private ArrayList<Integer> hourList;
    private ArrayList<Integer> minuteList;

    private Button setDailyButton, setWeeklyButton, setManualButton;

    private CheckBox checkMonday, checkTuesday, checkWednesday, checkThursday, checkFriday, checkSaturday, checkSunday;

    private LinearLayout weekListLayout;
    private TextView dailyTakeText;
    private LinearLayout manualSetLayout;
    private TextInputEditText setDateInputText;

    private AlarmSystemActivity.IntervalType intervalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SettingAlarmActivity", "Activity Sart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
        Log.d("SettingAlarmActivity", "after setcontentview");
        EdgeToEdge.enable(this);

        // findViewById를 통해 뷰를 찾습니다.
        View mainView = findViewById(R.id.main);
        if (mainView == null) {
            Log.e("SettingAlarmActivity", "mainView is null");
        } else {
            Log.d("SettingAlarmActivity", "mainView is not null");
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("SettingAlarmActivity", "Finish basic");
        saveButton = findViewById(R.id.Save_Button);

        nameInput = findViewById(R.id.NameEditInput);
        quantityInput = findViewById(R.id.QuantityEditInput);

        checkMonday = findViewById(R.id.CheckMonday);
        checkTuesday = findViewById(R.id.CheckTuesday);
        checkWednesday = findViewById(R.id.CheckWednesday);
        checkThursday = findViewById(R.id.CheckThursday);
        checkFriday = findViewById(R.id.CheckFriday);
        checkSaturday = findViewById(R.id.CheckSaturday);
        checkSunday = findViewById(R.id.CheckSunday);

        setDailyButton = findViewById(R.id.SetDailyButton);
        setWeeklyButton = findViewById(R.id.SetWeeklyButton);
        setManualButton = findViewById(R.id.SetManualButton);

        weekListLayout = findViewById(R.id.WeekListLayout);
        dailyTakeText = findViewById(R.id.DailyTakeText);
        manualSetLayout = findViewById(R.id.ManualSetLayout);
        setDateInputText = findViewById(R.id.SetDateInputText);

        layoutAlarms = findViewById(R.id.LayoutAlarms);
        addAlarmButton = findViewById((R.id.AddAlarmButton));

        //기초값 설정
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();
        intervalType = AlarmSystemActivity.IntervalType.daily;
        UpdateIntervalType(intervalType);

        //아래 세 개는 복용 간격 유형 변경 버튼 액션
        setDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervalType = AlarmSystemActivity.IntervalType.daily;
                UpdateIntervalType(intervalType);
            }
        });
        setWeeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervalType = AlarmSystemActivity.IntervalType.week;
                UpdateIntervalType(intervalType);
            }
        });
        setManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervalType = AlarmSystemActivity.IntervalType.manual;
                UpdateIntervalType(intervalType);
            }
        });

        //알람 시간대 추가 버튼 액션 / ShowTimePickerDialog() 확인
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTimePickerDialog();
            }
        });

        //알람 최종 저장 버튼 액션
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notificationName = nameInput.getText().toString();
                int quantity = Integer.parseInt(quantityInput.getText().toString());

                // 선택된 요일을 리스트로 저장
                ArrayList<Integer> selectedDays = new ArrayList<>();
                if (checkMonday.isChecked()) selectedDays.add(Calendar.MONDAY);
                if (checkTuesday.isChecked()) selectedDays.add(Calendar.TUESDAY);
                if (checkWednesday.isChecked()) selectedDays.add(Calendar.WEDNESDAY);
                if (checkThursday.isChecked()) selectedDays.add(Calendar.THURSDAY);
                if (checkFriday.isChecked()) selectedDays.add(Calendar.FRIDAY);
                if (checkSaturday.isChecked()) selectedDays.add(Calendar.SATURDAY);
                if (checkSunday.isChecked()) selectedDays.add(Calendar.SUNDAY);

                if (selectedDays.isEmpty() && intervalType == AlarmSystemActivity.IntervalType.week) {
                    Toast.makeText(SettingAlarmActivity.this, "적어도 하나의 요일을 선택해야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 결과를 MainActivity로 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("alarmName", notificationName);
                resultIntent.putExtra("alarmQuantity", quantity);
                resultIntent.putExtra("intervalType", AlarmSystemActivity.IntervalType.toInterger(intervalType));

                resultIntent.putExtra("alarmHours", hourList);
                resultIntent.putExtra("alarmMins", minuteList);
                resultIntent.putIntegerArrayListExtra("alarmDaysOnWeek", selectedDays);
                resultIntent.putExtra("manualIntervalDate", Integer.parseInt(setDateInputText.getText().toString()));

                setResult(1, resultIntent);
                finish(); // SecondActivity 종료
            }
        });
    }

    // 시간 설정 프래그먼트 표시
    private void ShowTimePickerDialog() {
        com.example.tempproject.TimePickerFragment timePickerFragment = new com.example.tempproject.TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // 알람 추가 메서드
    public void AddAlarm(Integer hour, Integer minute) {
        hourList.add(hour);
        minuteList.add(minute);
        updateAlarmList();
    }

    //복용 간격 방식 갱신
    private void UpdateIntervalType(AlarmSystemActivity.IntervalType nowType)
    {
        weekListLayout.setVisibility(View.GONE);
        dailyTakeText.setVisibility(View.GONE);
        manualSetLayout.setVisibility(View.GONE);

        switch (nowType)
        {
            case daily: dailyTakeText.setVisibility(View.VISIBLE); break;
            case week: weekListLayout.setVisibility(View.VISIBLE); break;
            case manual: manualSetLayout.setVisibility(View.VISIBLE); break;
        }
    }

    // 알람 목록 업데이트
    private void updateAlarmList() {
        layoutAlarms.removeAllViews();

        for (int i = 0; i < hourList.size(); i++) {
            final int index = i;
            String time = String.format("%02d:%02d", hourList.get(i), minuteList.get(i));

            TextView textView = new TextView(this);
            textView.setText(time);
            textView.setTextSize(18);
            textView.setPadding(16, 16, 16, 16);

            // 알람 삭제를 위한 길게 클릭 리스너
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 알람 삭제
                    hourList.remove(index);
                    minuteList.remove(index);
                    updateAlarmList();
                    return true;
                }
            });

            layoutAlarms.addView(textView);
        }

        // "알람 생성" 버튼을 가장 아래로 이동
        layoutAlarms.addView(addAlarmButton);
    }
}