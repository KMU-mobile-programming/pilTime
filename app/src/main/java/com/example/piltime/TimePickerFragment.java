package com.example.piltime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 현재 시간을 기본값으로 사용
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // TimePickerDialog 생성 및 반환
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    // 시간 설정 완료 시 호출되는 메서드
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // 설정된 시간을 SecondActivity로 전달하여 알람 추가
        SettingAlarmActivity activity = (SettingAlarmActivity) getActivity();
        if (activity != null) {

            activity.AddAlarm(hourOfDay, minute);
        }
    }
}