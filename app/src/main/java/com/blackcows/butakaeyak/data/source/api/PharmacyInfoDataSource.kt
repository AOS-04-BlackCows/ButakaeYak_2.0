package com.blackcows.butakaeyak.data.source.api

import android.util.Log
import com.blackcows.butakaeyak.data.models.PharmacyInfo
import com.blackcows.butakaeyak.data.retrofit.PharmacyInfoApiService
import javax.inject.Inject

class PharmacyInfoDataSource @Inject constructor(
    private val retrofit: PharmacyInfoApiService
) {
    private val tag = "PharmacyInfoDataSource"

    suspend fun searchPharmacy(Q0: String, Q1: String?, QT: Int?, QN: Int?, ORD: Int?): List<PharmacyInfo> {
        val list = mutableListOf<PharmacyInfo>()
        val result = retrofit.getPharmacyInfo(Q0, Q1, QT, QN, ORD)
        if(result.header.resultCode == "00") {
            result.body.items?.forEach {
                list.add(it.toPharmacyInfo())
            }
        } else {
            Log.d(tag, "SearchPharmacyInfo(code: ${result.header.resultCode}): ${result.header.resultMsg}")
        }

        return list
    }
}