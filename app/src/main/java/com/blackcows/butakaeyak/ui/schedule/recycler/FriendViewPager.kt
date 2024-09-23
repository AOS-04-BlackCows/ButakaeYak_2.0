package com.blackcows.butakaeyak.ui.schedule.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.ui.schedule.ScheduleDetailFragment
import com.blackcows.butakaeyak.ui.schedule.ScheduleFragment

class FriendViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()
    private val friendIdList = mutableListOf<String>()

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
    override fun containsItem(itemId: Long): Boolean {
        return friendIdList.contains(itemId.toString())
    }

    fun addFriend(friendId: String, isMine: Boolean) {
        val scheduleFragment = ScheduleDetailFragment.newInstance(friendId, isMine)
        fragmentList.add(scheduleFragment)
        friendIdList.add(friendId)

        notifyDataSetChanged()
    }

    fun removeFriend(friendId: String) {
        if(friendId.contains(friendId)) {
            val index = friendIdList.indexOf(friendId)
            friendIdList.removeAt(index)
            fragmentList.removeAt(index)
            notifyDataSetChanged()
        } else {
            throw Exception("FriendViewPager: 등록되지 않은 friendId 입니다.")
        }

    }

    fun getFragment(viewPager2: ViewPager2, friendId: String) {
        val index = friendIdList.indexOf(friendId)
        viewPager2.currentItem = index
    }
}