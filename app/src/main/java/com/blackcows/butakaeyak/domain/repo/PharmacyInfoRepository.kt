package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.PharmacyInfo

interface PharmacyInfoRepository {
    fun searchPharmacyInfo(name: String, callback: (List<PharmacyInfo>) -> (Unit))
}