package com.example.yactong.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.yactong.databinding.ActivitySignUpBinding
import com.example.yactong.firebase.firebase_store.FirestoreManager
import com.example.yactong.firebase.firebase_store.models.UserData

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val firestoreManager = FirestoreManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        with(binding) {
            btnSignup.setOnClickListener {
                var userDate = UserData("", 0, "")
                firestoreManager.trySignUp(userDate,
                    object : FirestoreManager.ResultListener {
                        override fun onSuccess() {
                            // 회원가입 성공 이벤트
                            Toast.makeText(
                                this@SignUpActivity,
                                "회원가입 성공",
                                Toast.LENGTH_LONG
                            ).show()
                            // 로그인에 전화번호 & 비밀번호 전달
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(intent)
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


//    private fun isSignUpValid() : Boolean {
//        with(binding) {
//            val inputId = inputId.text.toString()
//            val inputName = inputName.text.toString()
//            val inputAge = inputAge.text.toString()
//            val inputPw = inputPw.text.toString()
//
//            return inputId.isBlank() || inputName.isBlank() || inputAge.isBlank() || inputPw.isBlank()
//        }
//    }
}