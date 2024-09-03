package com.blackcows.butakaeyak

import android.os.Bundle
import android.view.View
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.blackcows.butakaeyak.firebase.auth.FirebaseAuthManager
import com.google.firebase.FirebaseApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomMenuBar.setupWithNavController(navController)


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
        if (state) binding.bottomMenuBar.visibility = View.GONE else binding.bottomMenuBar.visibility =
            View.VISIBLE
    }
}