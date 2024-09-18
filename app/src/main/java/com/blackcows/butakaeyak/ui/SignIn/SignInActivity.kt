package com.blackcows.butakaeyak.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
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
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivitySignInBinding
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val TAG = "SignInActivity"

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var localRepository: LocalRepository


    // 데이터 전달
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val id = result.data?.getStringExtra("id") ?: "none"
            val pw = result.data?.getStringExtra("pw") ?: "none"

            binding.inputId.setText(id)
            binding.inputPw.setText(pw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setSignUpTextView()

        Log.d(TAG, Utility.getKeyHash(this))

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

        val inPutId = intent.getStringExtra("id")
        val inPutPw = intent.getStringExtra("pw")

        binding.inputId.setText(inPutId)
        binding.inputPw.setText(inPutPw)
    }

    private fun initView() = with(binding) {
        btnVisibility.setOnClickListener {
            when(it.tag){
                "0" -> {
                    btnVisibility.tag = "1"
                    inputPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    btnVisibility.setImageResource(R.drawable.baseline_visibility_24dp)
                }
                "1" -> {
                    btnVisibility.tag = "0"
                    inputPw.transformationMethod = PasswordTransformationMethod.getInstance()
                    btnVisibility.setImageResource(R.drawable.baseline_visibility_off_24)
                }
            }
        }
        btnLogin.setOnClickListener {
            val userId = inputId.text.toString()
            val pw = inputPw.text.toString()
            if (validateInputs(userId, pw)) {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        binding.progressContainer.visibility = View.VISIBLE
                    }
//                    userRepository.loginWithId(userId, pw)
//                        .onSuccess {
//                            onSuccessLogin(it)
//                        }.onFailure {
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(this@SignInActivity, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
//                            }
//                        }

                    withContext(Dispatchers.Main) {
                        binding.progressContainer.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(
                    this@SignInActivity,
                    "아이디와 비밀번호를 입력해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
            Log.d(TAG, "아이디와 비밀번호 입력해주세요. ")
        }

        ivKakao.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    binding.progressContainer.visibility = View.VISIBLE
                }
                // 카카오톡 설치 확인
                // 설치 true -> 카카오톡 로그인
                // 설치 false -> 카카오 이메일 로그인
                // 로그인 실패하게 된다면 if문 타고 사용자가 취소했을 때는 그대로 return하여 login이 취소되고 -> 사용자가 취소한거 아니면 이메일 로그인
                val result = suspendCoroutine<Pair<OAuthToken?, Throwable?>> {
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@SignInActivity)) {
                        // 카카오톡 로그인
                        UserApiClient.instance.loginWithKakaoTalk(this@SignInActivity) { token, e ->
                            // 사용자 취소
                            if (e != null) {
                                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                                    Log.d(TAG, "카카오 로그인 사용자 취소")
                                    return@loginWithKakaoTalk
                                }
                                UserApiClient.instance.loginWithKakaoAccount(this@SignInActivity) { token, e ->
                                    it.resume(Pair(token, e))
                                }
                            } else if (token != null) {
                                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                            }
                        }
                    } else {
                        // 카카오 이메일 로그인
                        UserApiClient.instance.loginWithKakaoAccount(this@SignInActivity) { token, e ->
                            it.resume(Pair(token, e))
                        }
                    }
                }

                Log.d(TAG, "카카오 로그인: ${result.second?.message ?: "에러없음"}")

                //TODO: 나중에 flow로 바꿔서 처리하기...
                userRepository.trySignUpWithKakao()
//                    .onFailure {
//                        Log.d(TAG, it.message!!)
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(this@SignInActivity, it.message, Toast.LENGTH_SHORT).show()
//                            binding.progressContainer.visibility = View.GONE
//                        }
//
//                    }.onSuccess {
//                        onSuccessLogin(it)
//                        withContext(Dispatchers.Main) {
//                            binding.progressContainer.visibility = View.GONE
//                        }
//                    }
            }
        }
    }

    private suspend fun onSuccessLogin(userData: UserData) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SignInActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
        }
        if(binding.checkBox.isChecked) {
            localRepository.saveUserData(userData)
        }

        Log.d(TAG, "로그인 페이지로 이동")

        // 로그인에 아이디 & 비밀번호 전달
        val intent = Intent().apply {
            putExtra("userData", userData)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun validateInputs(id: String, password: String): Boolean {
        return id.isNotEmpty() && password.isNotEmpty()
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
                resultLauncher.launch(intent)
            }
        }
        // Span에 적용
        spannableString.setSpan(
            signUpClickableSpan,
            signUpStart,
            signUpEnd,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
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
