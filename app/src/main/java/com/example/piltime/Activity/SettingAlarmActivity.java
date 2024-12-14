package com.example.piltime.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.piltime.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//해야 하는 거: 요일은 됨. 이제 날짜 혹은 매일 알람으로 모드 바꾸는 것과 UI 변경, 세팅 변경 및 설정
public class SettingAlarmActivity extends AppCompatActivity {

    protected TextInputEditText nameInput;
    protected TextInputEditText quantityInput;

    protected Button alarmButtonBasic;
    protected Button addAlarmButton;
    protected ImageButton saveButton;
    protected ImageButton exitButton;

    protected LinearLayout layoutAlarms;
    protected ArrayList<Integer> hourList;
    protected ArrayList<Integer> minuteList;
    protected String startDateString;

    protected Button setDailyButton, setWeeklyButton, setManualButton;

    protected CheckBox checkMonday, checkTuesday, checkWednesday, checkThursday, checkFriday, checkSaturday, checkSunday;

    protected LinearLayout weekListLayout;
    protected TextView dailyTakeText;
    protected LinearLayout manualSetLayout;
    protected TextInputEditText setDateInputText;
    protected Button setStartDateButton;

    protected AlarmSystemActivity.IntervalType intervalType;

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
        exitButton = findViewById(R.id.Exit_Button);

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
        setStartDateButton = findViewById(R.id.SetStartDateButton);

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

        //시작 날짜 결정 버튼
        setStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePickerDialog();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //알람 최종 저장 버튼 액션
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //이하 연속되는 조건문은 예외처리들
                if(nameInput.getText().toString().equals(""))
                {
                    Toast.makeText(SettingAlarmActivity.this, "복용할 약의 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(quantityInput.getText().toString().equals(""))
                {
                    Toast.makeText(SettingAlarmActivity.this, "약의 갯수를 하나 이상 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(hourList.size() != minuteList.size())
                {
                    Toast.makeText(SettingAlarmActivity.this, "알림이 울릴 시간대를 지우고 다시 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(hourList.size() == 0)
                {
                    Toast.makeText(SettingAlarmActivity.this, "알림이 울릴 시간대를 하나 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String notificationName = nameInput.getText().toString();
                int quantity = Integer.parseInt(quantityInput.getText().toString());

                // 선택된 요일을 리스트로 저장
                ArrayList<Integer> selectedDays = new ArrayList<>();
                if (checkMonday.isChecked()) selectedDays.add(1);
                if (checkTuesday.isChecked()) selectedDays.add(2);
                if (checkWednesday.isChecked()) selectedDays.add(3);
                if (checkThursday.isChecked()) selectedDays.add(4);
                if (checkFriday.isChecked()) selectedDays.add(5);
                if (checkSaturday.isChecked()) selectedDays.add(6);
                if (checkSunday.isChecked()) selectedDays.add(7);

                if (selectedDays.isEmpty() && intervalType == AlarmSystemActivity.IntervalType.week) {
                    Toast.makeText(SettingAlarmActivity.this, "적어도 하나의 요일을 선택해야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (setDateInputText.getText().toString().equals("") || setDateInputText.getText().toString().equals("0"))
                {
                    if(intervalType == AlarmSystemActivity.IntervalType.manual)
                    {
                        Toast.makeText(SettingAlarmActivity.this, "적어도 1일 이상의 간격을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        setDateInputText.setText("0");
                    }
                }

                if(intervalType == AlarmSystemActivity.IntervalType.manual && startDateString==null)
                {
                    Toast.makeText(SettingAlarmActivity.this, "처음으로 복용하게 되는 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 결과를 AlarmSystemActivity로 전달
                Intent resultIntent = new Intent();
                resultIntent.putExtra("alarmName", notificationName);
                resultIntent.putExtra("alarmQuantity", quantity);
                resultIntent.putExtra("intervalType", AlarmSystemActivity.IntervalType.toInterger(intervalType));

                resultIntent.putExtra("alarmHours", hourList);
                resultIntent.putExtra("alarmMins", minuteList);
                resultIntent.putIntegerArrayListExtra("alarmDaysOnWeek", selectedDays);
                resultIntent.putExtra("manualIntervalDate", Integer.parseInt(setDateInputText.getText().toString()));
                resultIntent.putExtra("startDateString", startDateString);

                setResult(1, resultIntent);
                finish(); // SecondActivity 종료
            }
        });
    }

    // 시간 설정 프래그먼트 표시
    protected void ShowTimePickerDialog() {
        com.example.piltime.Activity.TimePickerFragment timePickerFragment = new com.example.piltime.Activity.TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //날짜 설정 프래그먼트 표시
    protected void ShowDatePickerDialog()
    {
        com.example.piltime.Activity.DatePickerFragment datePickerFragment = new com.example.piltime.Activity.DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // 알람 추가 메서드
    protected void AddAlarm(Integer hour, Integer minute) {
        hourList.add(hour);
        minuteList.add(minute);
        updateAlarmList();
    }

    //시작 날짜 설정 완료 함수
    protected void SetStartDate(Date nowStartDate)
    {
        startDateString = new SimpleDateFormat("yyyy-MM-dd").format(nowStartDate);
        setStartDateButton.setText(startDateString);
    }

    //복용 간격 방식 갱신
    protected void UpdateIntervalType(AlarmSystemActivity.IntervalType nowType)
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
    protected void updateAlarmList() {
        layoutAlarms.removeAllViews();

        ViewGroup parentLayout = findViewById(R.id.LayoutAlarms);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < hourList.size(); i++) {
            final int index = i;
            String time = String.format("%02d:%02d", hourList.get(i), minuteList.get(i));

            View view = inflater.inflate(R.layout.item_alarmtime, parentLayout, false);

            TextView textView = view.findViewById(R.id.textViewSettingAlarmTime);
            textView.setText(time);

            ImageButton imageButton = view.findViewById(R.id.buttonDeleteTime);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 알람 삭제
                    hourList.remove(index);
                    minuteList.remove(index);
                    updateAlarmList();
                }
            });

            layoutAlarms.addView(view);
        }

        // "알람 생성" 버튼을 가장 아래로 이동
        layoutAlarms.addView(addAlarmButton);
    }
}