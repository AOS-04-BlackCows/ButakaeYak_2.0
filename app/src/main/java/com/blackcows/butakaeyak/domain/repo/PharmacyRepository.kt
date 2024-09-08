package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData

interface PharmacyRepository {
    fun getMyPharmacy(): List<KakaoPlacePharmacy>
    fun saveMyPharmacy(myPharmacy: List<KakaoPlacePharmacy>)
    fun addMyPharmacy(pharmacy: KakaoPlacePharmacy)
    fun isPharmacyChecked(id: String) : Boolean
    fun removeMyPharmacy(id: String)
    fun saveAutoLoginData(userData: UserData)
    fun getSavedUserData(): UserData?
}