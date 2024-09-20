package com.blackcows.butakaeyak.ui.schedule.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.TodayGroupItemBinding

class TimeGroupRvAdapter(

): ListAdapter<MedicineGroup, TimeGroupRvAdapter.MedicineGroupViewHolder>(
    object: DiffUtil.ItemCallback<MedicineGroup>() {
        override fun areItemsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: MedicineGroup, newItem: MedicineGroup): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class MedicineGroupViewHolder(private val binding: TodayGroupItemBinding): ViewHolder(binding.root) {
        fun bind(item: MedicineGroup) {
            with(binding) {
                groupNameTv.text = item.name
                groupNameTv.text = "약 ${item.medicines.size}개"

                modifyBtn.setOnClickListener {
                    //TODO: MainNavigation.addFragment(
                    // MedicineGroupModifyingFragment(),
                    // FragmentTag.MedicineGroupModifyingFragment
                    // )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today_group_item, parent, false)
        return MedicineGroupViewHolder(TodayGroupItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MedicineGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}