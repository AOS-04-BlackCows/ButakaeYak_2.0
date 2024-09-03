package com.example.yactong.ui.SignIn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.intFloatMapOf
import androidx.core.content.ContextCompat
import com.example.yactong.Manifest
import com.example.yactong.databinding.ActivitySignUpBinding
import com.example.yactong.firebase.firebase_store.FirestoreManager
import com.example.yactong.firebase.firebase_store.models.UserData
import com.google.firebase.firestore.auth.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val firestoreManager = FirestoreManager()

    private lateinit var imageView : ImageView
    private lateinit var button : Button
    private var imageUri : Uri? = null

    private val userPhoneNumber by lazy { binding.inputPhoneNumber }
    private val userName by lazy { binding.inputName }
    private val userAge by lazy { binding.inputAge }
    private val userPw by lazy { binding.inputPw }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val phoneNumber = result.data?.getStringExtra("phoneNumber") ?: "none"
            val pw = result.data?.getStringExtra("pw") ?: "none"

            userPhoneNumber.setText(phoneNumber)
            userPw.setText(pw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupFocusChangeListeners()
        openGerally()
        getImg()
        setImg()
    }

    private fun initView() {
        with(binding) {
            btnSignup.setOnClickListener {
                if (validateAllFields()) {
                    var userData = UserData(
                        userPhoneNumber.text.toString(),
                        userAge.text.toString(),
                        userName.text.toString()
                    )
                    firestoreManager.trySignUp(userData,
                        object : FirestoreManager.ResultListener<Boolean> {
                            override fun onSuccess(result: Boolean) {
                                // 회원가입 성공 이벤트
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "회원가입 성공",
                                    Toast.LENGTH_LONG
                                ).show()

                                // 로그인에 전화번호 & 비밀번호 전달
                                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                                intent.putExtra("phoneNumber", userPhoneNumber.text.toString())
                                intent.putExtra("pw", userPw.text.toString())
                                resultLauncher.launch(intent)
                                finish()
                            }

                            override fun onFailure(e: Exception) {
                                //실패 시 처리
                                if(e.message == "이미 가입된 번호입니다") {
                                    binding.tiPhoneNumber.error = e.message
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "회원가입 실패: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        })
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "모든 필드를 올바르게 입력해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // focus 검사
    private fun setupFocusChangeListeners() {
        userPhoneNumber.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePhoneNumber()
            }
        }

        userName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateName()
            }
        }

        userAge.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateAge()
            }
        }

        userPw.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePw()
            }
        }
    }

    // 모든 입력 값을 한 번에 검사!
    private fun validateAllFields() : Boolean {
        var isValid = true
        isValid = validatePhoneNumber() && isValid
        isValid = validateName() && isValid
        isValid = validateAge() && isValid
        isValid = validatePw() && isValid

        return isValid
    }

    // 핸드폰 번호에 대한 유효성 검사
    private fun validatePhoneNumber() :Boolean {
        val phoneNumber = userPhoneNumber.text.toString()
        return if (phoneNumber.isEmpty() || !SignUpValidation.isValidPhoneNumber(phoneNumber)) {
            binding.tiPhoneNumber.error = "유효한 전화번호를 입력해주세요. (예: 010-1234-5678)"
            false
        } else {
            binding.tiPhoneNumber.error = null
            true
        }
    }

    // 이름에 대한 유효성 검사
    private fun validateName() : Boolean {
        val name = userName.text.toString()
        return if (name.isEmpty() || !SignUpValidation.isValidName(name)) {
            binding.tiName.error = "이름은 한글만 입력 가능합니다."
            false
        } else {
            binding.tiName.error = null
            true
        }
    }

    // 나이에 대한 유효성 검사
    private fun validateAge(): Boolean {
        val age = userAge.text.toString().toInt()
        return if (age == null || !SignUpValidation.isValidAge(age)) {
            binding.tiAge.error = "유효한 나이를 입력해주세요."
            false
        } else {
            binding.tiAge.error = null
            true
        }
    }

    // 비밀번호에 대한 유효성 검사
    private fun validatePw(): Boolean {
        val pw = userPw.text.toString()
        return if (pw.isEmpty() || pw.length < 6 || !SignUpValidation.isValidPw(pw)) {
            binding.tiPw.error = "비밀번호는 숫자, 영어와 특수문자 .!@#$ 만 가능합니다."
            false
        } else {
            binding.tiPw.error = null
            true
        }
    }

    // 프로필 사진 지정 -> 갤러리 open
    private fun openGerally() {

    }

    // 가져온 사진 보여주기
    private fun getImg() {

    }

    // 이미지 처리? 설정?
    private fun setImg() {

        val requestPermissionLauncher : ActivityResultLauncher<String> =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openGerally()
                }
            }

        val pickImgLauncher : ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data : Intent? = result.data
                    data?.data?.let {
                        imageUri = it
                        imageView.setImageURI(imageUri)
                    }
                }
            }

        title = "Edit Profile"
        imageView = binding.ivProfile

        // 이미지 동글게 표현하기
        imageView.clipToOutline = true
        button = binding.btnProfile
//        button.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == packageManager.PERMISSION_GRANTED)
//                openGerally()
//            button.text = null
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
    }
    private fun openGallery() {

    }
}