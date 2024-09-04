package com.blackcows.butakaeyak.ui.take.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewCycleBinding
import com.blackcows.butakaeyak.ui.take.data.AlarmItem

class CycleAdapter(private val alarmList: MutableList<AlarmItem>) : RecyclerView.Adapter<CycleAdapter.CycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleViewHolder {
        val binding = ItemRecyclerviewCycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CycleViewHolder, position: Int) {
        val alarmItem = alarmList[position]

        holder.timeText.text = alarmItem.timeText
        holder.image
        // 요일 텍스트 색상 설정
        holder.dayClickListeners()

        holder.binding.ivDelete.setOnClickListener {
            removeAt(position)
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    inner class CycleViewHolder(val binding : ItemRecyclerviewCycleBinding) : RecyclerView.ViewHolder(binding.root) {

        val image = binding.ivCycleOneAlarm
        val timeText = binding.tvCycleOneAlarmTime
        val monday = binding.tvMon
        val tuesday = binding.tvTue
        val wednesday = binding.tvWed
        val thursday = binding.tvThu
        val friday = binding.tvFri
        val saturday = binding.tvSat
        val sunday = binding.tvSun

        private var isMonSelected = false
        private var isTueSelected = false
        private var isWedSelected = false
        private var isThuSelected = false
        private var isFriSelected = false
        private var isSatSelected = false
        private var isSunSelected = false

        fun dayClickListeners() {
            monday.setOnClickListener { selectDay(monday, ::isMonSelected::set) }
            tuesday.setOnClickListener { selectDay(tuesday, ::isTueSelected::set) }
            wednesday.setOnClickListener { selectDay(wednesday, ::isWedSelected::set) }
            thursday.setOnClickListener { selectDay(thursday, ::isThuSelected::set) }
            friday.setOnClickListener { selectDay(friday, ::isFriSelected::set) }
            saturday.setOnClickListener { selectDay(saturday, ::isSatSelected::set) }
            sunday.setOnClickListener { selectDay(sunday, ::isSunSelected::set) }
        }

        private fun selectDay(textView: TextView, setSelected: (Boolean) -> Unit) {
            val isSelected = textView.currentTextColor == ContextCompat.getColor(itemView.context, R.color.green)
            setSelected(!isSelected)
            textView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (isSelected) R.color.dark_gray else R.color.green
                )
            )
        }
    }

    private fun removeAt(position: Int) {
        alarmList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, alarmList.size)
    }
}