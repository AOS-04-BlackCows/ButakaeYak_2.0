package com.blackcows.butakaeyak.ui.take

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.blackcows.butakaeyak.R
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val notificationTitle = intent.getStringExtra("NOTIFICATION_TITLE")
        val notificationContent = intent.getStringExtra("NOTIFICATION_CONTENT")

        Log.d("AlarmReceiver", "Alarm received: ID = $notificationId, Title = $notificationTitle, Content = $notificationContent")

        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.icon_logo)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())

        // 다음날 같은 시간으로 알람 설정
//        scheduleNextAlarm(context, intent)
    }
}