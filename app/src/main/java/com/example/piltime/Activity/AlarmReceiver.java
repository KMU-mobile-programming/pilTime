package com.example.piltime.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.piltime.R;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        //알람 스와이프로 알람 체크
        if(Objects.equals(action, "ACTION_NOTIFICATION_DELETE"))
        {
            Log.d("AlarmReceiver", "알림이 삭제되었습니다.");

            int notificationId = intent.getIntExtra("notificationId", -1);
            // 필요한 경우 추가 작업 수행
            // 예: 데이터베이스 업데이트, 통계 수집 등
        }

        Log.d("Alarm", "Alarm is Called");
        // 알람이 발생했을 때 실행될 코드 작성
        String alarmName = intent.getStringExtra("alarmName");

        // 알림(Notification)을 생성하여 사용자에게 알람이 울렸음을 알려줍니다.
        showNotification(context, alarmName);
    }

    private void showNotification(Context context, String alarmName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "alarm_channel";

        // 알림 ID 생성 (고유한 값으로 설정)
        int notificationId = (int) System.currentTimeMillis();

        // 안드로이드 오레오(API 26) 이상에서는 알림 채널이 필요합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "알람 채널",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null); // 알림 소리 설정

            notificationManager.createNotificationChannel(channel);
        }

        //알람 스와이프 시 복용 표시 확인
        Intent deleteIntent = new Intent(context, AlarmReceiver.class);
        deleteIntent.setAction("ACTION_NOTIFICATION_DELETE");
        deleteIntent.putExtra("notificationId", notificationId);
        PendingIntent pendingDeleteIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.background_rounded_corners)// 앱 아이콘이나 알람 아이콘 사용
                .setContentTitle("알람")
                .setContentText(alarmName + " 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setDeleteIntent(pendingDeleteIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        // 알림 표시
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            Log.d("AlarmReceiver", "알림이 표시되었습니다.");
        } else {
            Log.e("AlarmReceiver", "NotificationManager가 null입니다.");
        }
    }
}
