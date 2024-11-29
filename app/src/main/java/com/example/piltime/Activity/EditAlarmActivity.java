package com.example.piltime.Activity;

import android.content.Intent;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EditAlarmActivity extends SettingAlarmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();
        String testName = data.getStringExtra("alarmName");
        nameInput.setText(data.getStringExtra("alarmName"));
        quantityInput.setText(String.valueOf(data.getIntExtra("alarmQuantity", 999)));
        intervalType = AlarmSystemActivity.IntervalType.fromInteger(data.getIntExtra("intervalType", 0));
        UpdateIntervalType(intervalType);

        ArrayList<Integer> tempHours = data.getIntegerArrayListExtra("alarmHours");
        ArrayList<Integer> tempMins = data.getIntegerArrayListExtra("alarmMins");
        for(int i=0; i < tempHours.size(); i++) { AddAlarm(tempHours.get(i), tempMins.get(i)); }

        switch (intervalType)
        {
            case week:
                for(int i: data.getIntegerArrayListExtra("alarmDaysOnWeek"))
                {
                    switch (i)
                    {
                        case 1: checkMonday.setChecked(true); break;
                        case 2: checkTuesday.setChecked(true); break;
                        case 3: checkWednesday.setChecked(true); break;
                        case 4: checkThursday.setChecked(true); break;
                        case 5: checkFriday.setChecked(true); break;
                        case 6: checkSaturday.setChecked(true); break;
                        case 7: checkSunday.setChecked(true); break;
                    }
                }
                break;

            case manual:
                setDateInputText.setText(String.valueOf(data.getIntExtra("manualIntervalDate",0)));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    SetStartDate(dateFormat.parse(data.getStringExtra("startDateString")));
                } catch (ParseException e) {}
                break;
        }
    }
}
