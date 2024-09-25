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
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())

        // 다음날 같은 시간으로 알람 설정
//        scheduleNextAlarm(context, intent)
    }

//    private fun scheduleNextAlarm(context: Context, intent: Intent) {
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
//        val title = intent.getStringExtra("NOTIFICATION_TITLE")
//        val content = intent.getStringExtra("NOTIFICATION_CONTENT")
//        val hour = intent.getIntExtra("Hour", 0)
//        val minute = intent.getIntExtra("Minute",0)
//
//        // 다음날 알람을 위한 Intent 생성
//        val newIntent = Intent(context, AlarmReceiver::class.java).apply {
//            putExtra("NOTIFICATION_ID", notificationId)
//            putExtra("NOTIFICATION_TITLE", title)
//            putExtra("NOTIFICATION_CONTENT", content)
//            putExtra("ALARM_HOUR", hour)
//            putExtra("ALARM_MINUTE", minute)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context, notificationId, newIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
//        )
//
//        Log.d("alarmHour",hour.toString())
//        Log.d("alarmMinute",minute.toString())
//
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, hour)
//            set(Calendar.MINUTE, minute+1)
//            set(Calendar.SECOND, 0)
//        }
//
//        alarmManager.cancel(pendingIntent)
//
//        Log.d("AlarmReceiver", "Scheduling next alarm: Time = ${calendar.time}, ID = $notificationId")
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            Log.d("buildTest", "okay")
//            if (alarmManager?.canScheduleExactAlarms() == true) {
//                // 다음 알람 설정
//                val alarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
//                alarmManager.setAlarmClock(alarmClock, pendingIntent)
//            }
//        }
//    }
}