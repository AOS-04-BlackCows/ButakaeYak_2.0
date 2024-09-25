package com.blackcows.butakaeyak.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.ItemHistorysBinding
import com.bumptech.glide.Glide

private const val TAG = "HistoryRecyclerAdapter"
class HistoryRecyclerAdapter(private val clickListener: ClickListener) :
    ListAdapter<Medicine, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Medicine>() {
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return when {
                    oldItem is Medicine && newItem is Medicine ->
                        oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
                return oldItem == newItem
            }
        }

        private const val TYPE_MEDICINE = 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Medicine -> TYPE_MEDICINE
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MEDICINE -> {
                val medicineBinding =
                    ItemHistorysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MedicineResultHolder(medicineBinding)
            }
//            TYPE_FEED -> {}
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        runCatching {
            when (val item = getItem(position)) {
                is Medicine -> (holder as MedicineResultHolder).bind(item)
//                is ListItem.FeedItem -> (holder as FeedHolder).bind(item)
            }
        }.onFailure { exception ->
            Log.e("VideoListAdapter", "Exception! ${exception.message}")
        }
    }

    inner class MedicineResultHolder(medicineView: ItemHistorysBinding) :
        RecyclerView.ViewHolder(medicineView.root) {
        private val ivMedicine: ImageView = medicineView.ivMedicine
        private val tvMedicineName: TextView = medicineView.tvMedicineName
        private val tvMedicineType: TextView = medicineView.tvMedicineType
//        private val cbMyMedicine: CheckBox = medicineView.cbMyMedicine
        private val loMedicineInfo: ConstraintLayout = medicineView.loMedicineInfo

        fun bind(medicineItem: Medicine) {
//            val isSaved = clickListener.isMedicineChecked(medicineItem)
//            Log.d("HomeRecyclerView", "Name: ${medicineItem.name}, $isSaved")

            with(medicineItem) {

                if(imageUrl?.isNotEmpty() == true) {
                    Glide.with(itemView).load(imageUrl).into(ivMedicine)
                }

                tvMedicineName.text = name
                tvMedicineType.text = effect
                loMedicineInfo.setOnClickListener {
                    clickListener.onItemClick(medicineItem)
                    Log.d(TAG, "${name}")
                }
                //TODO 체크 한것들 한번에
//                cbMyMedicine.setOnCheckedChangeListener { buttonView, isChecked ->
//                    Log.d(TAG, "cbMyMedicine.isChecked: ${cbMyMedicine.isChecked}")
//                }

            }
        }
    }
    interface ClickListener{
        fun onItemClick(item: Medicine)
//        fun onItemlLongClick(item: Medicine)
//        fun isMedicineChecked(item: Medicine) : Boolean
//        fun setMedicineChecked(item: Medicine, isChecked:Boolean)
    }
}