package com.example.piltime.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.piltime.Activity.AlarmSystemActivity.AlarmForm;
import com.example.piltime.Activity.AlarmSystemActivity.DailyAlarm;
import com.example.piltime.Activity.AlarmSystemActivity.IntervalType;
import com.example.piltime.AlarmReceiver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlarmUtils {

    public static Map<Integer, ArrayList<Integer>> manualRequestCodesMap;

    public static void setAlarms(Context context, AlarmForm alarmForm) {
        // AlarmManager 인스턴스 가져오기
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int alarmId = alarmForm.hashCode();

        manualRequestCodesMap = new HashMap<>();

        // IntervalType에 따라 알람 설정
        if (alarmForm.intervalType == IntervalType.daily) {
            // 매일 알람 설정
            setDailyAlarms(context, alarmManager, alarmId, alarmForm);
        } else if (alarmForm.intervalType == IntervalType.week) {
            // 주간 알람 설정
            setWeeklyAlarms(context, alarmManager, alarmId, alarmForm);
        } else if (alarmForm.intervalType == IntervalType.manual) {
            // 매뉴얼 간격 알람 설정
            setManualAlarms(context, alarmManager, alarmId, alarmForm);
        }
    }

    private static void setDailyAlarms(Context context, AlarmManager alarmManager, int alarmId, AlarmForm alarmForm) {
        for (DailyAlarm dailyAlarm : alarmForm.dailyAlarms) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, dailyAlarm.hour);
            calendar.set(Calendar.MINUTE, dailyAlarm.minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // 이미 지난 시간이라면 다음 날로 설정
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarmName", alarmForm.name);

            int requestCode = generateUniqueRequestCode(alarmId, dailyAlarm, 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // 알람 설정
            setExactAlarm(alarmManager, calendar, pendingIntent);
        }
    }

    private static void setWeeklyAlarms(Context context, AlarmManager alarmManager, int alarmId, AlarmForm alarmForm) {
        for (DailyAlarm dailyAlarm : alarmForm.dailyAlarms) {
            for (int dayOfWeek : alarmForm.daysOnWeek) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, dailyAlarm.hour);
                calendar.set(Calendar.MINUTE, dailyAlarm.minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("alarmName", alarmForm.name);

                int requestCode = generateUniqueRequestCode(alarmId, dailyAlarm, dayOfWeek);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                // 알람 설정
                setExactAlarm(alarmManager, calendar, pendingIntent);
            }
        }
    }

    private static void setManualAlarms(Context context, AlarmManager alarmManager, int alarmId, AlarmForm alarmForm) {
        // 시작 날짜 설정 (예: alarmForm.manualStartDate)
        LocalDate startDate = LocalDate.now(); // 시작 날짜를 현재 날짜로 가정
        int intervalDays = alarmForm.manualIntervalDate;

        ArrayList<Integer> hashCodes = new ArrayList<>();

        // 원하는 기간 동안 알람 설정
        int totalDays = 365; // 1년간 설정한다고 가정
        for (int i = 0; i < totalDays; i += intervalDays) {
            LocalDate alarmDate = startDate.plusDays(i);

            for (DailyAlarm dailyAlarm : alarmForm.dailyAlarms) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(alarmDate.getYear(), alarmDate.getMonthValue() - 1, alarmDate.getDayOfMonth(),
                        dailyAlarm.hour, dailyAlarm.minute, 0);

                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    continue; // 이미 지난 시간은 건너뜁니다.
                }

                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("alarmName", alarmForm.name);

                int requestCode = generateUniqueRequestCode(alarmId, dailyAlarm, i);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                hashCodes.add(requestCode);

                // 알람 설정
                setExactAlarm(alarmManager, calendar, pendingIntent);
            }
        }
        manualRequestCodesMap.put(alarmId, hashCodes);
    }

    @SuppressLint("ScheduleExactAlarm")
    private static void setExactAlarm(AlarmManager alarmManager, Calendar calendar, PendingIntent pendingIntent) {
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }
        }
    }

    public static void cancelAlarms(Context context, AlarmForm alarmForm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int alarmId = alarmForm.hashCode();

        for (DailyAlarm dailyAlarm : alarmForm.dailyAlarms) {
            // IntervalType에 따라 취소 방식 결정
            if (alarmForm.intervalType == IntervalType.daily) {
                int requestCode = generateUniqueRequestCode(alarmId, dailyAlarm, 0);
                cancelAlarm(context, alarmManager, requestCode);
            } else if (alarmForm.intervalType == IntervalType.week) {
                for (int dayOfWeek : alarmForm.daysOnWeek) {
                    int requestCode = generateUniqueRequestCode(alarmId, dailyAlarm, dayOfWeek);
                    cancelAlarm(context, alarmManager, requestCode);
                }
            } else if (alarmForm.intervalType == IntervalType.manual) {
                // 매뉴얼 알람 취소
                // 설정한 모든 requestCode를 알고 있어야 함
                // 여기서는 간단히 alarmManager.cancelAll()을 호출할 수 없음
                // 실제 구현에서는 저장된 모든 requestCode를 반복하여 취소해야 함
                ArrayList<Integer> hashCodes = manualRequestCodesMap.get(alarmId);

                for(Integer requestCode: hashCodes)
                {
                    cancelAlarm(context, alarmManager, requestCode);
                }

                manualRequestCodesMap.remove(alarmId);
            }
        }
    }

    private static void cancelAlarm(Context context, AlarmManager alarmManager, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private static int generateUniqueRequestCode(int alarmId, DailyAlarm dailyAlarm, int identifier) {
        return alarmId + dailyAlarm.hour * 100 + dailyAlarm.minute + identifier * 1000;
    }
}

