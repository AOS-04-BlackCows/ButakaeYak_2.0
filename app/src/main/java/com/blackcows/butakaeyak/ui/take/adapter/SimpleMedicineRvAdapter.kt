package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.MedicineSimpleItemBinding
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.bumptech.glide.Glide

class SimpleMedicineRvAdapter: ListAdapter<Medicine, SimpleMedicineRvAdapter.SimpleMedicineViewHolder>(
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

    inner class SimpleMedicineViewHolder(private val binding: MedicineSimpleItemBinding): ViewHolder(binding.root) {
        fun bind(item: Medicine) {
            with(binding) {
                val imageUrl = item.imageUrl
                if(imageUrl?.isNotEmpty() == true) {
                    Glide.with(itemView).load(imageUrl).into(medicineIv)
                }
                medicineNameTv.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleMedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicine_simple_item, parent, false)
        return SimpleMedicineViewHolder(MedicineSimpleItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: SimpleMedicineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}