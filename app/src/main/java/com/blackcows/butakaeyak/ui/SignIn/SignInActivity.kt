package com.blackcows.butakaeyak.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivitySignInBinding
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.user.UserFragment


class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val firestoreManager = FirestoreManager()
    private val TAG = "SignIpActivity"


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
                val phoneNumber = inputPhoneNumber.text.toString()

                val pw = inputPw.text.toString()
                if (validateInputs(phoneNumber, pw)) {
                    firestoreManager.trySignIn(userPhoneNumber,
                        object : FirestoreManager.ResultListener<UserData> {
                            override fun onSuccess(result: UserData) {
                                // 회원가입 성공 이벤트
                                Toast.makeText(
                                    this@SignInActivity,
                                    "로그인 성공",
                                    Toast.LENGTH_LONG
                                ).show()

                                // MainActivity로 이동하고 UserFragment를 표시
                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                intent.putExtra("navigateTo", "user")
                                startActivity(intent)
                                finish()
                                Log.d(TAG, "로그인 페이지로 이동")
                            }

                            override fun onFailure(e: Exception) {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "로그인 실패: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                } else {
                    Toast.makeText(
                        this@SignInActivity,
                        "전화번호와 비밀번호를 입력해주세요.",
                        Toast.LENGTH_LONG)
                        .show()
                }
                Log.d(TAG, "전화번호와 비밀번호 입력해주세요. ")
            }
        }
    }

    private fun validateInputs(phoneNumber: String, password: String): Boolean {
        return phoneNumber.isNotEmpty() && password.isNotEmpty()
    }

    private fun setSignUpTextView() {

        // TextView 참조
        val textSignUp: TextView = binding.tvSignup

        // 전체 텍스트 설정
        val fullText = "계정이 없으신가요? 회원가입"
        val spannableString = SpannableString(fullText)

        // "회원가입" 텍스트의 시작 위치와 끝 위치 계산
        val signUpStart = fullText.indexOf("회원가입")
        val signUpEnd = signUpStart + "회원가입".length
        Log.d(TAG, "회원가입 시작 위치: $signUpStart, 끝 위치: $signUpEnd")

        // "회원가입" 텍스트에 클릭 이벤트 설정
        val signUpClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.d(TAG, "회원가입 텍스트 클릭됨")
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
        // Span에 적용
        spannableString.setSpan(
            signUpClickableSpan,
            signUpStart,
            signUpEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        // 색상 설정
        val signUpColor =
            ForegroundColorSpan(ContextCompat.getColor(this@SignInActivity, R.color.back_700))
        Log.d(TAG, "회원가입 텍스트 색상: $signUpColor")
        // Span 적용
        spannableString.setSpan(
            signUpColor,
            signUpStart,
            signUpEnd,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        // TextView에 적용
        textSignUp.movementMethod = LinkMovementMethod.getInstance() // 클릭 가능하게 설정
        textSignUp.text = spannableString
    }
}