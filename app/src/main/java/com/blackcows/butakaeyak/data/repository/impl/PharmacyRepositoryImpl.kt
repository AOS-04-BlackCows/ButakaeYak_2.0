package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.domain.repo.PharmacyRepository
import javax.inject.Inject

class PharmacyRepositoryImpl @Inject constructor(
    private val pharmacyDataSource: LocalDataSource
): PharmacyRepository {
    override fun getMyPharmacy(): List<KakaoPlacePharmacy> {
        return pharmacyDataSource.getMyPharmacy()
    }

    override fun saveMyPharmacy(myPharmacy: List<KakaoPlacePharmacy>) {
        return pharmacyDataSource.saveMyPharmacy(myPharmacy)
    }

    override fun addMyPharmacy(pharmacy: KakaoPlacePharmacy) {
        val addList = getMyPharmacy().toMutableList().apply {
            add(pharmacy)
        }

        saveMyPharmacy(addList)
    }

    override fun isPharmacyChecked(id: String): Boolean {
        return getMyPharmacy().any {
            it.id == id
        }
    }
}