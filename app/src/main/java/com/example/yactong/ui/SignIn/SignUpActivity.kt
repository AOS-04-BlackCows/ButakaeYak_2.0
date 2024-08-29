package com.example.yactong.ui.SignIn

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.yactong.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }
    private fun initView() {
        with(binding) {

        }
    }

    //실시간으로 체크 Text Watcher

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