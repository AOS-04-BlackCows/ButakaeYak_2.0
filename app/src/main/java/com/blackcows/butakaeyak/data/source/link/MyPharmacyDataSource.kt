package com.blackcows.butakaeyak.data.source.link

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MyPharmacy

interface MyPharmacyDataSource {
    fun getMyPharmacies(userId: String): List<MyPharmacy>
    fun saveMyPharmacies(pharmacies: List<MyPharmacy>)
    fun addSinglePharmacy(pharmacy: MyPharmacy)
    fun removePharmacy(pharmacy: MyPharmacy)
}