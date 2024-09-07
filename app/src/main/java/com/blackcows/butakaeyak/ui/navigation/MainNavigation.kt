package com.blackcows.butakaeyak.ui.navigation

import android.util.Log
import android.view.View
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
    private val TAG = "MainNavigation"
    private lateinit var fragmentManager: FragmentManager

    private var currentTab = TabTag.Take
    private val fragmentStack = HashMap<TabTag, Stack<FragmentTag>>()

    private lateinit var binding: ActivityMainBinding

    //navigation bar 안 보이게 할 때 쓰는 메소드
    fun hideBottomNavigation(state: Boolean) {
        binding.bottomMenuBar.visibility = if (state) View.GONE
                                            else  View.VISIBLE

        Log.d(TAG, "Hide Bottom Bar: $state")
    }


    fun addFragment(fragment: Fragment, tag: FragmentTag, enterAni: Int? = null, exitAni: Int? = null) {
        val transaction = fragmentManager.beginTransaction()

        if (enterAni != null && exitAni != null) {
            transaction.setCustomAnimations(R.anim.alpha,R.anim.none)
        }

        transaction.replace(R.id.fragment_container_view, fragment, tag.name).commit()

        Log.d("Navigation", "Push Fragment: ${tag.name} to Stack: ${currentTab.name}")
        fragmentStack[currentTab]!!.push(tag)
    }

    fun popCurrentFragment(enterAni: Int? = null, exitAni: Int? = null) {
        val curStack = fragmentStack[currentTab]!!
        val transaction = fragmentManager.beginTransaction()

        if(curStack.size == 0) return

        val curFragment = if(curStack.size == 1) {
             fragmentManager.findFragmentByTag(curStack[0].name)!!
        } else {
            val secondFromLastFragmentTag = curStack[curStack.lastIndex - 1]
            fragmentManager.findFragmentByTag(secondFromLastFragmentTag.name)!!
        }

        if(enterAni != null && exitAni != null) {
            transaction.setCustomAnimations(enterAni, exitAni)
        }

        transaction
            .remove(curFragment)
            .commitNow()

        Log.d("Navigation", "size: ${curStack.size}")

        curStack.pop()
    }



    fun toOtherTab(tabTag: TabTag) {
        currentTab = tabTag
        binding.viewPager.currentItem = tabTag.index
    }

    //-------------------------------------------------------------------------------------------------------------


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


    private fun initViewPager(activity: MainActivity, b: ActivityMainBinding) {
        binding = b
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
        binding.viewPager.isUserInputEnabled = false

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