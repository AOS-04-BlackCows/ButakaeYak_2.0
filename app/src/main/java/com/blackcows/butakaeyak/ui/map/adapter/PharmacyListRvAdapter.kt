package com.blackcows.butakaeyak.ui.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewPharmacyListBinding
import com.bumptech.glide.Glide

class PharmacyListRvAdapter(private val clickListener: ClickListener): ListAdapter<KakaoPlacePharmacy, PharmacyListRvAdapter.KakaoPlacePharmacyListViewHolder>(
        object: DiffUtil.ItemCallback<KakaoPlacePharmacy>() {
            override fun areItemsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.id == newItem.id)
            }
            override fun areContentsTheSame(oldItem: KakaoPlacePharmacy, newItem: KakaoPlacePharmacy): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }

        }
) {

    inner class KakaoPlacePharmacyListViewHolder(private val binding: ItemRecyclerviewPharmacyListBinding): ViewHolder(binding.root) {
        fun bind(item: KakaoPlacePharmacy) {
            with(binding) {
                tvPharmacyDistance.text = "${item.distance}m"
                tvPharmacyName.text = item.placeName
                tvPharmacyTelNum.text = item.phone
//                placeUrl.text = pharmacyItem.placeUrl
                tvPharmacyAddress.text = item.addressName
                tvPharmacyRoadAddress.text = item.roadAddressName
                btnFavoritePharmacy.setOnClickListener {
                    clickListener.onFavoriteClick(item)
                }
                btnCallPharmacy.setOnClickListener {
                    clickListener.onCallClick(item)
                }
                btnFindRoad.setOnClickListener {
                    clickListener.onFindRoadClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KakaoPlacePharmacyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_pharmacy_list, parent, false)
        return KakaoPlacePharmacyListViewHolder(ItemRecyclerviewPharmacyListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: KakaoPlacePharmacyListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    interface ClickListener {
        fun onFavoriteClick(item: KakaoPlacePharmacy)
        fun onCallClick(item: KakaoPlacePharmacy)
        fun onFindRoadClick(item: KakaoPlacePharmacy)
    }
}