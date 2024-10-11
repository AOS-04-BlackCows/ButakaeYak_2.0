package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy

interface PharmacyRepository {
    fun getMyPharmacy(): List<KakaoPlacePharmacy>
    fun saveMyPharmacy(myPharmacy: List<KakaoPlacePharmacy>)
    fun addMyPharmacy(pharmacy: KakaoPlacePharmacy)
    fun isPharmacyChecked(id: String) : Boolean
}