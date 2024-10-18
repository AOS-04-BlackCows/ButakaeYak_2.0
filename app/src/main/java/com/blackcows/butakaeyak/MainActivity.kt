package com.blackcows.butakaeyak

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.navigation.TabTag
import com.blackcows.butakaeyak.ui.state.LoginUiState
import com.blackcows.butakaeyak.ui.viewmodels.FriendViewModel
import com.blackcows.butakaeyak.ui.viewmodels.MemoViewModel
import com.blackcows.butakaeyak.ui.viewmodels.MyGroupViewModel
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_PERMISSION_LOCATION = 10
    private val TAG = "MainActivity"

    private val userViewModel: UserViewModel by viewModels()
    private val friendViewModel: FriendViewModel by viewModels()
    private val memoViewModel: MemoViewModel by viewModels()
    private val myGroupViewModel: MyGroupViewModel by viewModels()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainNavigation.initialize(this, binding)

        //TODO 알림 설정
        createNotificationChannel()
        // GPS 권한 체크 후 없을 시 요청
        checkPermissionForLocation()

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

        lifecycleScope.launch {
            userViewModel.loginUiState.collectLatest {
                Log.d("MainActivity", it.toString())
                when(it) {
                    is LoginUiState.Success -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Loading -> {
                        MainNavigation.showLoadingBar()
                    }

                    is LoginUiState.NotFoundAutoLoginData -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Failure -> {
                        MainNavigation.disableLoadingBar()
                        Toast.makeText(this@MainActivity, "로그인에 실패하였습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        MainNavigation.disableLoadingBar()
                    }
                }
            }
        }

        checkFirstLaunch()
        setUserObserver()

        userViewModel.autoLogin()
    }

    override fun onStart() {
        super.onStart()

        myGroupViewModel.getAllMedicineGroups(userViewModel.user.value?.id ?: "")
    }

    //TODO 알림 설정
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 완료")
            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
            }
        }
    }

    private fun checkFirstLaunch() {
        if(mainViewModel.isFirstLaunch()) {
            mainViewModel.setDefaultAlarm()

            val dialog
            = AlertDialog.Builder(this)
                .setTitle("기본 복용 시간을 설정해주세요.")
                .setPositiveButton("설정하러 가기") { dialog, _ ->
                    MainNavigation.toOtherTab(TabTag.User)
                    dialog.dismiss()
                }.setNeutralButton("나중에 하기") { dialog, _ ->
                    dialog.dismiss()
                }

            dialog.show()

            mainViewModel.setFirstLaunchFalse()
        }
    }

    private fun setUserObserver() {
        userViewModel.user.observe(this) {
            Log.d("MainActivity", "user observer: ${it?.id ?: "guest"}")

            if(it != null) {
                friendViewModel.getAllFriendProfiles(it.id)
                memoViewModel.getAllMemos(it.id)
            } else {
                friendViewModel.clearScheduleProfiles()
                memoViewModel.clearMemos()
            }

            myGroupViewModel.getAllMedicineGroups(it?.id ?: "")
        }
    }
}