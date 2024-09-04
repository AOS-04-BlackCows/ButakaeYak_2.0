package com.blackcows.butakaeyak.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.home.PillResultFragment

class HomeViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    //TODO: update list.
    private val pages by lazy { listOf(PillResultFragment()) }//, FeedFragment()
    val pageTag = listOf(PillResultFragment.TAB_NAME)//,FeedFragment.TAB_NAME

    override fun getItemCount() = pages.size
    override fun createFragment(position: Int) = pages[position]
}