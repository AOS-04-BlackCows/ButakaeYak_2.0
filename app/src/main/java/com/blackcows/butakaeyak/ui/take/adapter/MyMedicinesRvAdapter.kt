package com.blackcows.butakaeyak.ui.take.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.MyMedicineItemBinding
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime

class MyMedicinesRvAdapter:
    ListAdapter<Medicine, MyMedicinesRvAdapter.MedicineViewHolder>(
        object: DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.id == newItem.id)
            }
            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }
        }
    ) {

        inner class MedicineViewHolder(private val binding: MyMedicineItemBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Medicine) {
                //TODO: 이미지 넣어주기
//            GlideApp.with(binding.root)
//                .load(item.imageUrl)
//                .into(binding.image)
                with(binding) {
                    medicineNameTv.text = item.name
                    medicineEffectTv.text = item.effect

                    tvMedicineAlarmIv.setOnClickListener {
                        //TODO: navigate to MyMedicineDetailPage.
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_medicine_item, parent, false)
        return MedicineViewHolder(MyMedicineItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}