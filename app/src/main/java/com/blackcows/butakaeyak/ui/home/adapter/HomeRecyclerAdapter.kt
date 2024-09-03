package com.blackcows.butakaeyak.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.ui.home.data.ListItem

import com.blackcows.butakaeyak.ui.home.placeholder.PlaceholderContent.PlaceholderItem

class HomeRecyclerAdapter(private val values: List<PlaceholderItem>) :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return oldItem == newItem
            }
        }

        private const val TYPE_PIll = 0
        private const val TYPE_FEED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.PillResultItem -> TYPE_PIll
            is ListItem.FeedItem -> TYPE_FEED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            //TODO: 고치기
            TYPE_PIll -> PillResultHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_results, parent, false))

            TYPE_FEED -> FeedHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.PillResultItem -> (holder as PillResultHolder).bind(item)
            is ListItem.FeedItem -> (holder as FeedHolder).bind(item)
        }
    }

    inner class PillResultHolder(pillView: View) : RecyclerView.ViewHolder(pillView) {
//        val pillPicture: ImageView = pillView.
//        val pillName: TextView = binding.tvPillname
//        val pillType: TextView = binding.tvPilltype
//        val pillFavorite: ImageButton = binding.btnPillfavoriteadd

        fun bind(pillitem: ListItem.PillResultItem) {
            when (pillitem) {
            }

        }

    }

    inner class FeedHolder(feedView: View) : RecyclerView.ViewHolder(feedView) {
//        val feedthumb: ImageView = binding.feedIvThumb
//        val feedname: TextView = binding.feedTvName
//        val feedwriter: TextView = binding.feedTvWriter

        fun bind(pillitem: ListItem.FeedItem) {
            when (pillitem) {
            }

        }
    }
}