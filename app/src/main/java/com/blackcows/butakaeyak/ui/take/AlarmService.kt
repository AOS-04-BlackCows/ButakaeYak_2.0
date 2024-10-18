package com.blackcows.butakaeyak.ui.take

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
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

        startForeground(1, createNotification())

        // 알람 시간과 제목을 인텐트에서 가져옴
        val alarmTimeList = intent?.getSerializableExtra("ALARM_LIST") as? List<AlarmItem>
        val startDate = intent?.getLongExtra("START_DATE", System.currentTimeMillis())

        alarmTimeList?.forEach { alarm ->
            startDate?.let { scheduleAlarm(alarm, it) }
        }

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

        // Android 8.0 이상에서는 Notification Channel 생성 필수
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "약 복용 알람 서비스 채널"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        // 알림 생성
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("약 복용 알람")
            .setContentText("알람이 설정되었습니다.")
            .setSmallIcon(R.drawable.icon_logo)  // 유효한 아이콘 설정
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 중요도 설정
            .build()
    }

}
