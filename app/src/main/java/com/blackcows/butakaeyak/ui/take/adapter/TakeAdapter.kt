package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewTakeBinding
import com.blackcows.butakaeyak.ui.take.data.CycleItem


class TakeAdapter(private val deleteListener: OnItemDeleteListener, val context: Context): RecyclerView.Adapter<TakeAdapter.TakeHolder>() {

    private var mItems: MutableList<CycleItem> = mutableListOf()

    interface OnItemDeleteListener {
        fun onItemDelete(position: Int)
    }

    fun updateData(update: List<CycleItem>) {
        Log.d("CycleItemAdapter", "Updating data: $update")
        mItems.clear()
        mItems.addAll(update)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakeHolder {
        val binding = ItemRecyclerviewTakeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TakeHolder(binding)
    }

    override fun onBindViewHolder(holder: TakeHolder, position: Int) {
        val mItem = mItems[position]
        holder.bind(mItem, position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class TakeHolder(val binding : ItemRecyclerviewTakeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CycleItem, position: Int) {
            binding.ivMedicineForm.setImageBitmap(item.aImage)
            binding.tvMedicineName.text = item.aName

            binding.ivDelete.setOnClickListener {
                deleteListener.onItemDelete(position)
            }
        }

    }
}