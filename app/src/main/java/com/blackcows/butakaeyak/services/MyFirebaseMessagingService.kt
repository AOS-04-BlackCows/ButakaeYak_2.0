package com.blackcows.butakaeyak.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
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

        Log.d(TAG, "From: ${message.from}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${message.data}")
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        //TODO
        //  알림 직접 만들어서 푸쉬해줘야 할듯!
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
}