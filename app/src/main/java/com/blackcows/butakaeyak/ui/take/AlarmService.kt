package com.blackcows.butakaeyak.ui.take

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.icu.util.Calendar
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.ui.take.data.AlarmItem

class AlarmService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 알람 시간과 제목을 인텐트에서 가져옴
        val alarmTimeList = intent?.getSerializableExtra("ALARM_LIST") as? List<AlarmItem>
        val startDate = intent?.getLongExtra("START_DATE", System.currentTimeMillis())

        alarmTimeList?.forEach { alarm ->
            startDate?.let { scheduleAlarm(alarm, it) }
        }

        // Foreground로 실행해야 앱 종료 후에도 유지됨
        startForeground(1, createNotification())
        return START_STICKY
    }

    private fun scheduleAlarm(alarm: AlarmItem, startDate: Long) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = alarm.timeInMillis
        }

        Log.d("AlarmService","${alarm.timeInMillis}")
        val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
        val alarmMinute = calendar.get(Calendar.MINUTE)

        val adjustedTimeInMillis = startDate +
                (alarm.timeInMillis % (24 * 60 * 60 * 1000)) + (9 * 60 * 60 * 1000)

        // 현재 시간을 기준으로 알람 시간이 지나면 다음 날로 설정
        val delay = adjustedTimeInMillis - System.currentTimeMillis()
        if (delay <= 0) {
            scheduleNextDayAlarm(alarm)
            return
        }

        runnable = Runnable {
            triggerAlarm(alarm) // 알람 트리거
            scheduleNextDayAlarm(alarm) // 다음 날 같은 시간에 반복 설정
        }

        handler.postDelayed(runnable, delay)
    }

    private fun scheduleNextDayAlarm(alarm: AlarmItem) {
        val nextDayMillis = 24 * 60 * 60 * 1000 // 하루(24시간) 후
        handler.postDelayed(runnable, nextDayMillis.toLong())
    }

    private fun triggerAlarm(alarm: AlarmItem) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", alarm.requestCode)
            putExtra("NOTIFICATION_TITLE", "약 먹을 시간입니다.")
        }
        sendBroadcast(notificationIntent) // 알람 트리거
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val notificationChannelId = "alarm_service_channel"
        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("약 복용 알람")
            .setSmallIcon(R.drawable.icon_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        return notification
    }
}
