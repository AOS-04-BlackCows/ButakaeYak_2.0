package com.blackcows.butakaeyak.ui.home.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentFeedListBinding
import com.blackcows.butakaeyak.databinding.ItemResultsBinding
import com.blackcows.butakaeyak.ui.home.data.ListItem
import com.bumptech.glide.Glide

// 이전 코드 -                        values: MutableList<PlaceholderItem>
// 안되는 코드 class HomeRecyclerAdapter(private val onClick: (ListItem) -> Unit)
class HomeRecyclerAdapter :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                return when {
                    oldItem is ListItem.PillResultItem && newItem is ListItem.PillResultItem ->
                        oldItem.itemSeq == newItem.itemSeq

                    oldItem is ListItem.FeedItem && newItem is ListItem.FeedItem ->
                        oldItem.name == newItem.name

                    else -> false
                }
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
            TYPE_PIll -> {
                val pillbinding =
                    ItemResultsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PillResultHolder(pillbinding)
            }
//            TYPE_FEED -> {}
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        runCatching {
            when (val item = getItem(position)) {
                is ListItem.PillResultItem -> (holder as PillResultHolder).bind(item)
                is ListItem.FeedItem -> (holder as FeedHolder).bind(item)
            }
        }.onFailure { exception ->
            Log.e("VideoListAdapter", "Exception! ${exception.message}")
        }
    }

    inner class PillResultHolder(pillView: ItemResultsBinding) :
        RecyclerView.ViewHolder(pillView.root) {
        private val ivPill: ImageView = pillView.ivPill
        private val tvPillname: TextView = pillView.tvPillname
        private val tvPilltype: TextView = pillView.tvPilltype
        private val btnFavoritepill: ImageButton = pillView.btnFavoritepill
        private val btnMypill: ImageButton = pillView.btnMypill

        fun bind(pillitem: ListItem.PillResultItem) {
            with(pillitem) {
                Glide.with(itemView).load(R.drawable.choco).into(ivPill)
                tvPillname.text = itemName
                tvPilltype.text = entpName
                btnFavoritepill.setOnClickListener {
                    Log.d("아이템 좋아요 누름","${itemName}")
//                    onClick(this)
                }
                btnMypill.setOnClickListener {
                    Log.d("아이템 복용약 누름","${entpName}")
//                    onClick(this)
                }
            }
        }
    }

    inner class FeedHolder(feedView: FragmentFeedListBinding) :
        RecyclerView.ViewHolder(feedView.root) {
//        val feedthumb: ImageView = binding.feedIvThumb
//        val feedname: TextView = binding.feedTvName
//        val feedwriter: TextView = binding.feedTvWriter

        fun bind(pillitem: ListItem.FeedItem) {
            when (pillitem) {
            }

        }
    }
}