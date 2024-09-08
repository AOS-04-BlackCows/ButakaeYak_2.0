package com.blackcows.butakaeyak.ui.user

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.databinding.MypageItemFavoriteBinding

class UserFavoriteViewPagerAdapter(val items : MutableList<KakaoPlacePharmacy>, val localDataSource : LocalDataSource) : RecyclerView.Adapter<UserFavoriteViewPagerAdapter.ViewPagerHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFavoriteViewPagerAdapter.ViewPagerHolder {
        val binding = MypageItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserFavoriteViewPagerAdapter.ViewPagerHolder,
        position: Int
    ) {
        val pharmacy = items[position]
        holder.aImage.setImageResource(R.drawable.pharmacy)
        holder.aName.text = pharmacy.placeName
        holder.aGroup.text = pharmacy.phone

        //TODO BindViewHolder 과부화 에러 수정
        holder.button.setOnClickListener {
            Handler(Looper.getMainLooper()).post {
                removeItem(pharmacy.id, position)
            }
        }
    }

    override fun getItemCount(): Int {
         return items.size
    }

    inner class ViewPagerHolder(private val binding : MypageItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        val aImage = binding.ivMedicine
        val aName = binding.tvMedicineNameInfo
        val aGroup = binding.tvMedicineGroupInfo
        val button = binding.ivDelete
    }

    //TODO 약국 데이터 지우기
    private fun removeItem(id: String, position: Int) {
        // LocalDataSource에서 아이템 제거
        localDataSource.removeMyPharmacy(id)
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}