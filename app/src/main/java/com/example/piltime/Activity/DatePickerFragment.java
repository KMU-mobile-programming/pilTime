package com.example.piltime.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 현재 시간을 기본값으로 사용
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // TimePickerDialog 생성 및 반환
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // 시간 설정 완료 시 호출되는 메서드
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 설정된 시간을 SecondActivity로 전달하여 알람 추가
        SettingAlarmActivity activity = (SettingAlarmActivity) getActivity();
        if (activity != null) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DATE, dayOfMonth);
            Date date = calendar.getTime();

            activity.SetStartDate(date);
        }
    }
}
