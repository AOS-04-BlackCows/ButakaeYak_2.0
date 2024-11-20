package com.blackcows.butakaeyak.ui.friend.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.databinding.ItemFriendlistBinding


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
        runCatching {
            when(val item = getItem(position)){
                is Friend -> (holder as FriendListHolder).bind(item)
            }
        }.onFailure {
            exception ->
            Log.e("FriendRecyclerAdapter", "Exception! ${exception.message}")
        }
    }

    inner class FriendListHolder(friendView : ItemFriendlistBinding) :
        RecyclerView.ViewHolder(friendView.root){
        private val tvKakaoName: TextView = friendView.kakaoName
        private val tvNikename: TextView = friendView.butakaeyakNikename

        fun bind(friendItem: Friend) {
            val isSaved = clickListener.isfriendChecked(friendItem)
            with(friendItem){
                tvKakaoName.text = id
                tvNikename.text = proposer
            }
        }
    }

    interface ClickListener{
        fun isfriendChecked(item : Friend)
    }
}