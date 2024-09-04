package com.blackcows.butakaeyak.data.retrofit

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.dto.DrugInfoDto
import com.blackcows.butakaeyak.data.dto.PharmacyListInfoDTO
import com.blackcows.butakaeyak.data.dto.PillInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PharmacyInfoApiService {
    @GET("ErmctInsttInfoInqireService/getParmacyListInfoInqire")
    suspend fun getPharmacyInfo(
        @Query("Q0") q0: String?, // 시도
        @Query("Q1") q1: String?, // 시군구
        @Query("QT") qt: Int?, // 진료 요일
        @Query("QN") qn: Int?, // 약국 이름
        @Query("ORD") ord: Int?, // 순서?
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("ServiceKey") serviceKey: String = BuildConfig.PHARMACY_LIST_INFO_KEY
    ): PharmacyListInfoDTO
}