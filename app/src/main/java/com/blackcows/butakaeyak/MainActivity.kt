package com.blackcows.butakaeyak

import android.os.Bundle
import android.view.View
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.kakao.sdk.user.UserApiClient
import com.blackcows.butakaeyak.firebase.auth.FirebaseAuthManager

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)


        // 인텐트에서 navigateTo 값을 가져옴
        val navigateTo = intent.getStringExtra("navigateTo")
        Log.d(TAG, "navigateTo: $navigateTo")

        if (navigateTo == "user") {
            Log.d(TAG, "Navigating to UserFragment")
            navController.navigate(R.id.navigation_user)
        }
    }

    //navigation bar 안 보이게 할 때 쓰는 메소드
    fun hideBottomNavigation(state: Boolean) {
        if (state) binding.navView.visibility = View.GONE else binding.navView.visibility =
            View.VISIBLE
    }
}