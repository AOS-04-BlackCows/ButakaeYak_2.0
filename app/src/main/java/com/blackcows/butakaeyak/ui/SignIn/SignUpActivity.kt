package com.blackcows.butakaeyak.ui.SignIn

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blackcows.butakaeyak.databinding.ActivitySignUpBinding
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.firebase.firestore.auth.User

class SignUpActivity : AppCompatActivity() {

    private val TAG = "SignUpActivity"

    private lateinit var binding: ActivitySignUpBinding

    private val firestoreManager = FirestoreManager()

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private var imageUri: Uri? = null

    private val userId by lazy { binding.inputId }
    private val userName by lazy { binding.inputName }
    private val userPw by lazy { binding.inputPw }

    // 데이터 전달
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val id = result.data?.getStringExtra("id") ?: "none"
            val pw = result.data?.getStringExtra("pw") ?: "none"

            userId.setText(id)
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
        setImgUri()
    }

    private fun initView() {
        with(binding) {
            btnSignup.setOnClickListener {
                if (validateAllFields()) {
                    var userData = UserData(
                        userName.text.toString(),
                        userId.text.toString(),
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
                                intent.putExtra("id", userId.text.toString())
                                intent.putExtra("pw", userPw.text.toString())
                                resultLauncher.launch(intent)
                                finish()
                            }

                            override fun onFailure(e: Exception) {
                                //실패 시 처리
                                if (e.message == "이미 가입된 번호입니다") {
                                    binding.tiId.error = e.message
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "회원가입 실패: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        })
                }
            }
        }
    }

    // focus 검사
    private fun setupFocusChangeListeners() {
        userId.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateId()
            }
        }

        userName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateName()
            }
        }

        userPw.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePw()
            }
        }
    }

    // 모든 입력 값을 한 번에 검사!
    private fun validateAllFields(): Boolean {
        var isValid = true
        isValid = validateId() && isValid
        isValid = validateName() && isValid
        isValid = validatePw() && isValid

        return isValid
    }

    // 핸드폰 번호에 대한 유효성 검사
    private fun validateId(): Boolean {
        val id = userId.text.toString()
        return if (id.isEmpty() || !SignUpValidation.isValidId(id)) {
            binding.tiId.error = "Id는 영어 숫자만 가능합니다."
            false
        } else {
            binding.tiId.error = null
            true
        }
    }

    // 이름에 대한 유효성 검사
    private fun validateName(): Boolean {
        val name = userName.text.toString()
        return if (name.isEmpty() || !SignUpValidation.isValidName(name)) {
            binding.tiName.error = "이름은 한글만 입력 가능합니다."
            false
        } else {
            binding.tiName.error = null
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

    // 갤러리 open
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(TAG, "권한 승인 : $isGranted")
            if (isGranted) {
                openGallery()
            }
        }

    // 가져온 사진 보여주기
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "결과 코드 : ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    Log.d(TAG, "이미지 URI : $it")
                    imageUri = it
                    imageView.setImageURI(imageUri)
                }
            }
        }

    private fun setImgUri() {
        title = "Edit Profile"
        imageView = binding.ivProfile
        imageView.clipToOutline = true
        button = binding.btnProfile
        button.setOnClickListener {
            Log.d(TAG, "버튼 클릭 이벤트 발생!!")
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) // 에러 왜 뜨는지..
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "이미지 권한이 승인됨")
                openGallery()
                button.text = null
            } else {
                Log.d(TAG, "권한 요청 중")
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        Log.d(TAG, "갤러리 오픈!!!!")
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }
}