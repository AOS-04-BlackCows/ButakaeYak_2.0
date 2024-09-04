package com.blackcows.butakaeyak.ui.take.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime

class TodayMedicineRvAdapter: ListAdapter<MedicineAtTime, TodayMedicineRvAdapter.MedicineAtTimeViewHolder> {
    inner class MedicineAtTimeViewHolder(private val binding: TodayMedicineItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MedicineAtTime) {
//            GlideApp.with(binding.root)
//                .load(item.imageUrl)
//                .into(binding.image)
            with(binding) {

            }
        }
    }
}