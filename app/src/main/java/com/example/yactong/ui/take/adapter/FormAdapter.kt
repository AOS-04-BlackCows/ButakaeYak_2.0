package com.example.yactong.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yactong.databinding.ItemRecyclerviewFormBinding
import com.example.yactong.ui.take.data.FormItem

class FormAdapter(val mItems:MutableList<FormItem>,val context: Context):RecyclerView.Adapter<FormAdapter.FormHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormHolder {
        val binding = ItemRecyclerviewFormBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FormHolder(binding)
    }

    override fun onBindViewHolder(holder: FormHolder, position: Int) {
        Glide.with(holder.image.context)
            .load(mItems[position].aImage)
            .into(holder.image)
        holder.name.text = mItems[position].aName
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class FormHolder(val binding : ItemRecyclerviewFormBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivMedicineForm
        val name = binding.tvMedicineFormName
    }
}