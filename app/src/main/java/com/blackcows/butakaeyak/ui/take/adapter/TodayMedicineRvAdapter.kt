package com.blackcows.butakaeyak.ui.take.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.TodayMedicineItemBinding
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime

class TodayMedicineRvAdapter:
    ListAdapter<MedicineAtTime, TodayMedicineRvAdapter.MedicineAtTimeViewHolder>(
        object: DiffUtil.ItemCallback<MedicineAtTime>() {
            override fun areItemsTheSame(oldItem: MedicineAtTime, newItem: MedicineAtTime): Boolean {
                //Log.d("DiffUtil", "old: ${oldItem.imageUrl}, new: ${newItem.imageUrl}}")
                return  (oldItem.time == newItem.time) && (oldItem.list == newItem.list)
            }
            override fun areContentsTheSame(oldItem: MedicineAtTime, newItem: MedicineAtTime): Boolean {
                //Log.d("DiffUtil", "old is same with new: ${oldItem == newItem}")
                return oldItem == newItem
            }

        }
    ) {
    inner class MedicineAtTimeViewHolder(private val binding: TodayMedicineItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MedicineAtTime) {
//            GlideApp.with(binding.root)
//                .load(item.imageUrl)
//                .into(binding.image)
            with(binding) {
                val visibleItems = mutableListOf<Medicine>()
                alarmTimeTv.text = item.time

                visibleItems.clear()
                visibleItems.addAll(item.list.take(2))

                // 남은 아이템이 있으면 "더보기" 버튼 표시
                expandButton.visibility = if (visibleItems.size > 2) View.VISIBLE else View.GONE

                expandButton.setOnClickListener {
                    updateVisibleItemList(fullItemList.size)
                    expandButton.visibility = View.GONE
                    submitList()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineAtTimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_medicine_item, parent, false)
        return MedicineAtTimeViewHolder(TodayMedicineItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineAtTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}