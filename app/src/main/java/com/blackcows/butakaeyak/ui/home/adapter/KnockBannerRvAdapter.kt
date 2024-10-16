package com.blackcows.butakaeyak.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.databinding.KnockBannerItemBinding
import com.bumptech.glide.Glide

data class KnockBanner(
    val name: String,
    val profileUrl: String,
    val uid: String,
)

class KnockBannerRvAdapter(
    private val onKnockClick: (KnockBanner) -> Unit
): ListAdapter<KnockBanner, KnockBannerRvAdapter.BannerViewHolder>(
    object: DiffUtil.ItemCallback<KnockBanner>() {
        override fun areItemsTheSame(oldItem: KnockBanner, newItem: KnockBanner): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: KnockBanner, newItem: KnockBanner): Boolean {
            return oldItem == newItem
        }

    }
) {
    inner class BannerViewHolder(private val binding: KnockBannerItemBinding): ViewHolder(binding.root) {
        fun bind(item: KnockBanner) = with(binding){
            if(item.profileUrl.isNotEmpty())
                Glide.with(itemView).load(item.profileUrl).into(profileIv)

            nameTv.text = item.name

            knockBtn.setOnClickListener {
                onKnockClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = KnockBannerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}