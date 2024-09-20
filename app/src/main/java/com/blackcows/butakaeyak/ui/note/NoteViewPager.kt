package com.blackcows.butakaeyak.ui.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.home.HomeFragment
import com.blackcows.butakaeyak.ui.map.MapFragment
import com.blackcows.butakaeyak.ui.search.SearchFragment
import com.blackcows.butakaeyak.ui.user.UserFragment

class NoteViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(NoteGroupFragment(), NoteDateFragment())

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}