package com.example.piltime.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.piltime.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AlarmSystemActivity extends AppCompatActivity {

    //private ActivityResultLauncher

    public enum IntervalType
    {
        daily, week, manual;
        public static IntervalType fromInteger(int x) {
            switch(x) {
                case 0: return daily;
                case 1: return week;
                case 2: return manual;
            }
            return null;
        }

        public static int toInterger(IntervalType x) {
            switch(x) {
                case daily: return 0;
                case week: return 1;
                case manual: return 2;
            }
            return 99;
        }
    }

    public class AlarmForm
    {
        public int quantity;
        public String name;
        public IntervalType intervalType;
        public ArrayList<Integer> daysOnWeek;
        public int manualIntervalDate;
        public ArrayList<DailyAlarm> dailyAlarms;
        public LocalDate startDate;
    }

    public class DailyAlarm
    {
        public int hour;
        public int minute;
        public boolean isTake;
    }

    private TextView titleDateText;

    private Button createButton;
    private Button fixButton;

    private ScrollView showTimeScroll;
    private LinearLayout showTimeScrollLayout;
    private Button showAlarmButtonBasic;
    private Button mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton, sundayButton;

    ArrayList<Integer> tempDaysOnWeek;

    public ArrayList<AlarmForm> alarms;
    private AlarmForm edittingAlarm;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarmsystem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleDateText = findViewById(R.id.TitleDateText);
        createButton = (Button) findViewById(R.id.Create_Button);
        fixButton = findViewById(R.id.Fix_Button);
        showTimeScroll = findViewById(R.id.ShowTimeScroll);
        showTimeScrollLayout = findViewById(R.id.ShowTimeScrollLayout);
        showAlarmButtonBasic = findViewById(R.id.ShowAlarmButtonBasic);

        mondayButton = findViewById(R.id.MondayButton);
        tuesdayButton = findViewById(R.id.TuesdayButton);
        wednesdayButton = findViewById(R.id.WednesdayButton);
        thursdayButton = findViewById(R.id.ThursdayButton);
        fridayButton = findViewById(R.id.FridayButton);
        saturdayButton = findViewById(R.id.SaturdayButton);
        sundayButton = findViewById(R.id.SundayButton);

        alarms = new ArrayList<AlarmForm>();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이제 여기에 넘어가는 생성하러 넘어가는 버튼 필요
                Intent intent = new Intent(getApplicationContext(), com.example.piltime.Activity.SettingAlarmActivity.class);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
            }
        });

        fixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ShowAlarmListFragment(); }
        });

        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(1);
            }
        });
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(2);
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(3);
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(4);
            }
        });
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(5);
            }
        });
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(6);
            }
        });
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWeekofDay(7);
            }
        });

        Log.e("AlarmSystemActivity", "onCreate is ended");
    }

    public void SelectWeekofDay(int dayofWeek)
    {
        int gapofDate = dayofWeek - LocalDate.now().getDayOfWeek().getValue();
        selectedDate = LocalDate.now().plusDays(gapofDate);
        ResetScreen(selectedDate);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        selectedDate = LocalDate.now();
        ResetScreen(selectedDate);
    }

    //수정하거나 삭제할 수 있는 알람 목록
    private void ShowAlarmListFragment() {
        AlarmListFragment fragment = new AlarmListFragment(alarms);
        fragment.setOnAlarmActionListener(new AlarmListFragment.OnAlarmActionListener() {
            @Override
            public void onAlarmEdit(AlarmForm alarm) {
                // 알람 수정 액션
                editAlarm(alarm);
            }

            @Override
            public void onAlarmDelete(AlarmForm alarm) {
                // 알람 삭제 액션
                deleteAlarm(alarm);
            }
        });

        fragment.show(getSupportFragmentManager(), "alarms");
    }

    private void editAlarm(AlarmForm alarm) {
        edittingAlarm = alarm;

        // 알람 수정 화면으로 이동
        Intent intent = new Intent(getApplicationContext(), com.example.piltime.Activity.EditAlarmActivity.class);

        intent.putExtra("alarmName", alarm.name);
        intent.putExtra("alarmQuantity", alarm.quantity);
        intent.putExtra("intervalType", AlarmSystemActivity.IntervalType.toInterger(alarm.intervalType));

        ArrayList<Integer> tempHours = new ArrayList<Integer>();
        ArrayList<Integer> tempmins = new ArrayList<Integer>();
        for (DailyAlarm dailyAlarm: alarm.dailyAlarms) {
            tempHours.add(dailyAlarm.hour);
            tempmins.add(dailyAlarm.minute);
        }
        intent.putIntegerArrayListExtra("alarmHours", tempHours);
        intent.putIntegerArrayListExtra("alarmMins", tempmins);

        intent.putIntegerArrayListExtra("alarmDaysOnWeek", alarm.daysOnWeek);
        intent.putExtra("manualIntervalDate", alarm.manualIntervalDate);
        if(alarm.startDate != null) {intent.putExtra("startDateString", alarm.startDate.toString());}
        startActivityForResult(intent, 2);
    }

    private void deleteAlarm(AlarmForm alarm) {

        AlarmUtils.cancelAlarms(this, alarm);
        alarms.remove(alarm);

        ResetScreen(selectedDate);
    }

    public void ChangeIsTake(DailyAlarm dailyAlarm)
    {

    }

    //화면 갱신 함수
    public void ResetScreen(LocalDate selectedDate)
    {
        //상단의 날짜 갱신
        String nowDate = String.format("%02d월 %02d일", selectedDate.getMonthValue(), selectedDate.getDayOfMonth());
        titleDateText.setText(nowDate);

        //1. 현재 alarms가 가지고 있는 모든 AlarmForms 객체들 검색. 조건은 다음과 같음
        // 1-1. 만약 alarms가 매일이거나,
        // 1-2. 날짜 간격 결과가 selectedDate와 일치하거나,
        // 1-3. selectedDate의 요일과 일치하면
        //2. 일치하는 AlarmForms에서 모든 DailyAlarm 추출
        //3. DailyAlarm의 hours와 minutes을 추출해서 ShowTimeScroll에 등록

        //오늘의 알람 초기화
        showTimeScrollLayout.removeAllViews();

        for(int i = 0; i < alarms.size(); i++)
        {
            AlarmForm checkingAlarm = alarms.get(i);
            Boolean isOK = false;

            switch (checkingAlarm.intervalType)
            {
                case daily: isOK = true; break;
                case week:
                    for (int n = 0; n < checkingAlarm.daysOnWeek.size(); n++)
                    {
                        if(checkingAlarm.daysOnWeek.get(n) == selectedDate.getDayOfWeek().getValue())
                        {
                            isOK = true;
                            break;
                        }
                    } break;

                //그 이후 선택된 날짜와 첫 복용 날짜, 날짜 간격 고려해서 그 날이 복용하는 날인지 확인
                case manual:
                    if(selectedDate.isAfter(checkingAlarm.startDate) || selectedDate.isEqual(checkingAlarm.startDate))
                    {
                        long daysBetween = ChronoUnit.DAYS.between(checkingAlarm.startDate, selectedDate);
                        if(daysBetween % checkingAlarm.manualIntervalDate == 0) {isOK = true;}
                    }
                    break;
            }

            //만약 지금 AlarmForm이 조건에 맞지 않으면 다음 AlarmForm 검색
            if(isOK == false) {continue;}

            for(int t = 0; t < checkingAlarm.dailyAlarms.size(); t++)
            {
                final int index = t;
                final int alarmIndex = i;

                DailyAlarm nowDailyAlarm = checkingAlarm.dailyAlarms.get(t);

                String time = String.format("%02d:%02d", nowDailyAlarm.hour, nowDailyAlarm.minute);

                Button newButton = new Button(this);
                newButton.setText(time);
                newButton.setLayoutParams(new ViewGroup.LayoutParams(showAlarmButtonBasic.getLayoutParams()));

                if(alarms.get(alarmIndex).dailyAlarms.get(index).isTake) {newButton.setBackgroundColor(Color.GREEN);}
                else {newButton.setBackgroundColor(Color.RED);}

                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alarms.get(alarmIndex).dailyAlarms.get(index).isTake
                                = !alarms.get(alarmIndex).dailyAlarms.get(index).isTake;

                        if(alarms.get(alarmIndex).dailyAlarms.get(index).isTake) {newButton.setBackgroundColor(Color.GREEN);}
                        else {newButton.setBackgroundColor(Color.RED);}
                    }
                });

                showTimeScrollLayout.addView(newButton);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != 1) {return;}

        AlarmForm alarmForm = new AlarmForm();
        if(requestCode == 2)
        {
            alarmForm = edittingAlarm;
            AlarmUtils.cancelAlarms(this, alarmForm);
            edittingAlarm = null;
        }

        if (requestCode == 1 || requestCode == 2)
        {

            alarmForm.dailyAlarms = new ArrayList<DailyAlarm>();
            alarmForm.name = data.getStringExtra("alarmName");
            alarmForm.quantity = data.getIntExtra("alarmQuantity", 0);
            alarmForm.intervalType = IntervalType.fromInteger(data.getIntExtra("intervalType", 0));
            alarmForm.manualIntervalDate = data.getIntExtra("manualIntervalDate", 0);
            alarmForm.daysOnWeek = data.getIntegerArrayListExtra("alarmDaysOnWeek");

            ArrayList<Integer> tempHours = data.getIntegerArrayListExtra("alarmHours");
            ArrayList<Integer> tempMins = data.getIntegerArrayListExtra("alarmMins");

            for(int i = 0; i < tempHours.size(); i++)
            {
                DailyAlarm dailyAlarm = new DailyAlarm();
                dailyAlarm.hour = tempHours.get(i);
                dailyAlarm.minute = tempMins.get(i);
                dailyAlarm.isTake = false;
                alarmForm.dailyAlarms.add(dailyAlarm);
            }

            String tempDate = data.getStringExtra("startDateString");
            if(tempDate != null) {alarmForm.startDate = LocalDate.parse(tempDate);}


            Log.d("AlarmSystemActivity", "completeDailyAlarm");

            if(requestCode == 1) { alarms.add(alarmForm); }

            AlarmUtils.setAlarms(this, alarmForm);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}