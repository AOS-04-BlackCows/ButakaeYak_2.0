package com.blackcows.butakaeyak.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.home.HomeFragment
import com.blackcows.butakaeyak.ui.search.SearchFragment
import com.blackcows.butakaeyak.ui.map.MapFragment
import com.blackcows.butakaeyak.ui.note.NoteFragment
import com.blackcows.butakaeyak.ui.schedule.ScheduleFragment
import com.blackcows.butakaeyak.ui.user.UserFragment

class MainViewpager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(HomeFragment(), ScheduleFragment(), NoteFragment(), UserFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}