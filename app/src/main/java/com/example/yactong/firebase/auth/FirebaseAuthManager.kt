package com.example.yactong.firebase.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

object FirebaseAuthManager {
    private const val TIME_OUT = 60L
    private const val TAG = "FirebaseAuthManager"

    private var storedVerificationId = ""

    private val auth = FirebaseAuth.getInstance().apply {
        setLanguageCode("KR")
    }

    private val callbacks = object: OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "Verification Start")
        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // 문자로 여섯 자리 숫자 코드를 보낸 뒤 호출되는 함수.
            Log.d(TAG, "Please Enter the Code.")
            storedVerificationId = verificationId
        }
    }

    fun verifyPhoneNumber(context: Activity, phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(TIME_OUT, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun submitSmsCode(numberCode: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, numberCode)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}