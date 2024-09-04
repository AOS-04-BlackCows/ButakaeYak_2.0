package com.blackcows.butakaeyak.ui.take.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.MedicineSimpleItemBinding

class TodayMedicineLvAdapter(
    private val todayMedicines: List<Medicine>
): BaseAdapter() {

    override fun getCount(): Int = todayMedicines.size

    override fun getItem(position: Int): Any = todayMedicines[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = todayMedicines[position]
        val context = parent!!.context

        val binding = if(convertView != null) convertView.tag as MedicineSimpleItemBinding
            else  {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                inflater.inflate(R.layout.today_medicine_item, parent, false)
                MedicineSimpleItemBinding.inflate(inflater)
            }

        with(binding) {
            //TODO: Add Image.
            //medicineIv = glide

            medicineNameTv.text = item.name
        }

        return binding.root
    }
}