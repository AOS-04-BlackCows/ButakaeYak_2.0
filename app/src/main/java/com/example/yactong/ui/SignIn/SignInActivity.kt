package com.example.yactong.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.yactong.R
import com.example.yactong.databinding.ActivitySignInBinding
import com.example.yactong.firebase.firebase_store.FirestoreManager
import com.example.yactong.firebase.firebase_store.models.UserData


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val firestoreManager = FirestoreManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setSignUpTextView()

        val inPutPhoneNumber = intent.getStringExtra("phoneNumber")
        val inPutPw = intent.getStringExtra("pw")

        binding.inputPhoneNumber.setText(inPutPhoneNumber)
        binding.inputPw.setText(inPutPw)


    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListener {
                val userPhoneNumber = inputPhoneNumber.text.toString()

                firestoreManager.trySignIn(userPhoneNumber,
                    object : FirestoreManager.ResultListener<UserData> {
                        override fun onSuccess(result: UserData) {
                            // 회원가입 성공 이벤트
                            Toast.makeText(
                                this@SignInActivity,
                                "로그인 성공",
                                Toast.LENGTH_LONG
                            ).show()
                            //val intent = Intent(this@SignInActivity, MyPage::class.java)
                            //startActivity(intent)

                            finish()
                        }

                        override fun onFailure(e: Exception) {
                            //실시간으로 체크 Text Watcher
                        }

                    }
                )
            }
        }
    }

    private fun setSignUpTextView() {
        with(binding) {

            val fullText = "계정이 없으신가요? 회원가입"
            val spannableString = SpannableString(fullText)

            val signUpStart = fullText.indexOf("회원가입")
            val signUpEnd = signUpStart + "회원가입".length

            val signUpClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }

            spannableString.setSpan(
                signUpClickableSpan,
                signUpStart,
                signUpEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )

            tvSignup.movementMethod = LinkMovementMethod.getInstance() // 클릭 가능하게 설정
            tvSignup.text = spannableString
        }
    }
}