package com.blackcows.butakaeyak

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import com.blackcows.butakaeyak.ui.navigation.MainViewpager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()
        initNavigation()

        // 인텐트에서 navigateTo 값을 가져옴
        val navigateTo = intent.getStringExtra("navigateTo")
        Log.d(TAG, "navigateTo: $navigateTo")

        if (navigateTo == "user") {
            Log.d(TAG, "Navigating to UserFragment")
            //navController.navigate(R.id.navigation_user)
        }
    }

    //navigation bar 안 보이게 할 때 쓰는 메소드
    fun hideBottomNavigation(state: Boolean) {
        if (state) binding.bottomMenuBar.visibility = View.GONE else binding.bottomMenuBar.visibility =
            View.VISIBLE
    }

    private fun initViewPager() {
        val viewPager = binding.viewPager
        val viewPagerAdapter = MainViewpager(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomMenuBar.menu.getItem(position).isChecked = true
            }
        })
    }
    private fun initNavigation() {
        binding.bottomMenuBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_take -> {
                    binding.viewPager.currentItem = 0
                }
                R.id.navigation_home -> {
                    binding.viewPager.currentItem = 1
                }
                R.id.navigation_map -> {
                    binding.viewPager.currentItem = 2
                }
                R.id.navigation_user -> {
                    binding.viewPager.currentItem = 3
                }
                else -> return@setOnItemSelectedListener false
            }

            true
        }
    }
}