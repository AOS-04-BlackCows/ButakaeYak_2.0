package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.models.PharmacyInfo

interface PharmacyInfoRepository {
    fun searchPharmacyInfo(name: String, callback: (List<PharmacyInfo>) -> (Unit))
}