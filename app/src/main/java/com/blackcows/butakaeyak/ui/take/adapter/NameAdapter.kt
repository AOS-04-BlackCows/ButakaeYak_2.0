package com.blackcows.butakaeyak.ui.take.adapter

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.databinding.ItemRecyclerviewNameBinding
import com.blackcows.butakaeyak.ui.getMedicineTypeToDrawable
import com.blackcows.butakaeyak.ui.take.adapter.NameAdapter.DetailViewHolder
import com.bumptech.glide.Glide

class NameAdapter(private val clickListener: ClickListener): ListAdapter<TakeAddMedicine, DetailViewHolder>(
    object: DiffUtil.ItemCallback<TakeAddMedicine>() {
        override fun areItemsTheSame(oldItem: TakeAddMedicine, newItem: TakeAddMedicine): Boolean {
            return (1 == 2)
        }
        override fun areContentsTheSame(oldItem: TakeAddMedicine, newItem: TakeAddMedicine): Boolean {
            return 1 == 2
        }
    }
) {
    inner class DetailViewHolder(private val binding: ItemRecyclerviewNameBinding): ViewHolder(binding.root) {
        fun bind(item: TakeAddMedicine, position: Int) {
            with(binding){
                Glide.with(root.context)
                    .load(getMedicineTypeToDrawable(item.imageUrl))
                    .into(rvBtnMedicineForm)

                etMedicineName.setText(item.name)
                rvBtnMedicineForm.setOnClickListener {
                    clickListener.onMedicineClick(item, position)
                }

                btnMinus.setOnClickListener {
                    Log.d("TAGTAGTAG", "${item.imageUrl}")
                    clickListener.onMinusClick(item, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_name, parent, false)
        return DetailViewHolder(ItemRecyclerviewNameBinding.bind(view))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    interface ClickListener {
        fun onMinusClick(item: TakeAddMedicine,position: Int)
        fun onMedicineClick(item: TakeAddMedicine,position: Int)
        fun onSearchClick(item: TakeAddMedicine)
    }
}