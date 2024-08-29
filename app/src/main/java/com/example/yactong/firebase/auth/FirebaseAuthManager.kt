package com.example.yactong.firebase.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class FirebaseAuthManager(
    private val smsVerificationListener: SmsVerificationListener
) {

    /**
     * 이 인증 과정은 FireBase가 사용자를 인증하는 과정임.
     * 따라서, 회원가입 뿐만 아니라 로그인을 할떄도 이 인증과정을 거쳐야함.
     * 로그아웃도 해줘야함.
     */

    /**
     * 입력된 전화 번호로 인증 시도.
     * @param phoneNumber: 인증 코드를 보낼 전화 번호
     */

    fun verifyPhoneNumber(context: Activity, phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(TIME_OUT, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * 입력된 숫자 코드를 이용해 인증 시도
     * @param numberCode: 인증을 위해 입력된 숫자 코드
     */

    fun submitSmsCode(numberCode: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, numberCode)
        signInWithPhoneAuthCredential(credential)
    }

    companion object {
        private const val TAG = "FirebaseAuthManager"
        private const val TIME_OUT = 60L        //60초 안에 SMS 코드 입력.

        /**
         *  Firebase에 로그인 되어 있나?
         */
        fun isSignIn(): Boolean {
            return Firebase.auth.currentUser != null
        }
        /**
         *  Firebase 로그아웃
         */
        fun signOut() {
            Firebase.auth.signOut()
        }
    }

    //----------------------------------------------------------------------------------------------------------
    // 이 아래는 안 봐도 됨
    //----------------------------------------------------------------------------------------------------------
    //Doc: https://firebase.google.com/docs/auth/android/phone-auth?hl=ko&authuser=0&_gl=1*r379bk*_ga*MTcxNjAxMjQ2MS4xNzI0ODIyMTAx*_ga_CW55HF8NVT*MTcyNDgzNDgzMS4yLjEuMTcyNDgzNTAxMy41MS4wLjA.


    private var storedVerificationId = ""

    private val auth = FirebaseAuth.getInstance().apply {
        setLanguageCode("KR")
    }


    private val callbacks = object: OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "Verification Start When Instance Verification or Auto-Retriever.")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.i(TAG, "Verification Failed.")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // 문자로 여섯 자리 숫자 코드를 보낸 뒤 호출되는 함수.
            Log.d(TAG, "Please Enter the Code.")
            storedVerificationId = verificationId
        }
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    smsVerificationListener.onSuccess()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.i(TAG, "signInWithCredential:failure", task.exception)
                    smsVerificationListener.onFail(task.exception?: java.lang.Exception())
                }
            }
    }

    interface SmsVerificationListener {
        fun onSuccess()
        fun onFail(e: Exception)
    }
}