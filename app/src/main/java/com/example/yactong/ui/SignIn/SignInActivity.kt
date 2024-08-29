package com.example.yactong.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListener {
                val userPhoneNumber = inputPhoneNumber.text.toString()

                firestoreManager.trySignIn(userPhoneNumber,
                    object : FirestoreManager.ResultListener {
                        override fun onSuccess() {
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
}