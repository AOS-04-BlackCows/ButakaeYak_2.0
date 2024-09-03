package com.blackcows.butakaeyak.ui.navigation

import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityMainBinding

object MainNavigation {
    private lateinit var mainActivity: MainActivity

    fun initialize(activity: MainActivity, binding: ActivityMainBinding) {
        mainActivity = activity

        val viewPager = binding.viewPager
        val viewPagerAdapter = MainViewpager(activity)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomMenuBar.menu.getItem(position).isChecked = true
            }
        })

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