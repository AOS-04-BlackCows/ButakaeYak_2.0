package com.blackcows.butakaeyak.ui.navigation

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import java.util.Stack

object MainNavigation {
    private lateinit var fragmentManager: FragmentManager

    private var currentTab = TabTag.Take
    private val fragmentStack = HashMap<TabTag, Stack<FragmentTag>>()


    fun initialize(activity: MainActivity, binding: ActivityMainBinding) {
        fragmentManager = activity.supportFragmentManager

        fragmentStack[TabTag.Take] = Stack()
        fragmentStack[TabTag.Search] = Stack()
        fragmentStack[TabTag.Map] = Stack()
        fragmentStack[TabTag.User] = Stack()

        initViewPager(activity, binding)
        initNavigation(binding)
        addBackPressedCallback(activity, binding)
    }

    fun addFragment(fragment: Fragment, tag: FragmentTag) {
        fragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment, tag.name).commit()

        Log.d("Navigation", "Push Fragment: ${tag.name} to Stack: ${currentTab.name}")
        fragmentStack[currentTab]!!.push(tag)
    }

    fun popCurrentFragment() {
        val curStack = fragmentStack[currentTab]!!
        if(curStack.size == 0) return

        val curFragment = fragmentManager.findFragmentByTag(
            curStack.lastElement().name
        )!!

        fragmentManager.beginTransaction()
            .remove(curFragment)
            .commit()

        Log.d("Navigation", "size: ${curStack.size}")
        fragmentStack[currentTab]!!.pop()
    }


    private fun initViewPager(activity: MainActivity, binding: ActivityMainBinding) {
        val viewPager = binding.viewPager
        val viewPagerAdapter = MainViewpager(activity)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomMenuBar.menu.getItem(position).isChecked = true
            }
        })
    }
    private fun initNavigation(binding: ActivityMainBinding) {
        binding.bottomMenuBar.setOnItemSelectedListener { item ->
            currentTab = when(item.itemId) {
                R.id.navigation_take -> {
                    binding.viewPager.currentItem = 0
                    TabTag.Take
                }
                R.id.navigation_home -> {
                    binding.viewPager.currentItem = 1
                    TabTag.Search
                }
                R.id.navigation_map -> {
                    binding.viewPager.currentItem = 2
                    TabTag.Map
                }
                R.id.navigation_user -> {
                    binding.viewPager.currentItem = 3
                    TabTag.User
                }
                else -> return@setOnItemSelectedListener false
            }

            true
        }
    }
    private fun addBackPressedCallback(activity: MainActivity, binding: ActivityMainBinding) {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val curStack = fragmentStack[currentTab]!!
                if(curStack.size == 0) {
                    if(currentTab == TabTag.Take) {
                        activity.finishAffinity()
                    } else {
                        currentTab = TabTag.Take
                        binding.viewPager.currentItem = 0
                    }
                } else popCurrentFragment()
            }
        }
        activity.onBackPressedDispatcher.addCallback(callback)
    }
}