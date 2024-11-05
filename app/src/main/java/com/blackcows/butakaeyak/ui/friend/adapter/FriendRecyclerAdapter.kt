package com.blackcows.butakaeyak.ui.friend.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.databinding.ItemFriendlistBinding
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter.ClickListener


class FriendRecyclerAdapter(private val clickListener: ClickListener) :
    ListAdapter<Friend, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

        companion object{
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Friend>(){
                override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                    return when {
                        oldItem is Friend && newItem is Friend ->
                            oldItem.id == newItem.id
                        else -> false
                    }
                }

                override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                    return oldItem == newItem
                }
            }

            private const val TYPE_FRIEND = 0
        }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FRIEND -> {
                val friendBinding =
                    ItemFriendlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                FriendListHolder(friendBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class FriendListHolder(friendView : ItemFriendlistBinding) :
        RecyclerView.ViewHolder(friendView.root){
        private val tvKakaoName: TextView = friendView.kakaoName
        private val ButakaeyakNikename: TextView = friendView.butakaeyakNikename
    }
}