package com.blackcows.butakaeyak.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = "MyFirebaseMessagingService"

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var localUtilsRepository: LocalUtilsRepository

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(TAG, "From: ${message.toString()}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            val data = message.data
            Log.d(TAG, "Message data payload: ${message.data}")
            showNotification(data["sender"] ?: "unknown")
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            //showNotification(it.body?: "unknown")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch {
            val loginData = localUtilsRepository.getAutoLoginData() ?: return@launch

            val result = if(loginData.isKakao) userRepository.loginWithKakaoId(loginData.kakaoId.toLong())
                        else userRepository.loginWithId(loginData.loginId, loginData.pwd)

            when(result) {
                is LoginResult.Success -> {
                    userRepository.registerDeviceToken(result.user)
                } else -> return@launch
            }
        }
    }

    private fun showNotification(from: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val intent = Intent(this, MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(this, channelId)
            .setAutoCancel(true)
            .setContentTitle("${from}님이 노크하셨어요!")
            .setContentText("얼른 드셔요.")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)


        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}