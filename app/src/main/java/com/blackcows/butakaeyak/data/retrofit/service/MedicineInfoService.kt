package com.blackcows.butakaeyak.data.retrofit.service

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.dto.DrugInfoDto
import com.blackcows.butakaeyak.data.dto.MedicineDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MedicineInfoService {
    @GET("getDrugPrdtPrmsnDtlInq05")
    suspend fun getMedicineInfo(
        @Query("item_name") name: String,
        @Query("pageNo") page: Int = 1,
        @Query("numOfRows") rows: Int = 1,
        @Query("type") type: String = "json",
        @Query("serviceKey") key: String = BuildConfig.MEDICINE_INFO_KEY,
    ): MedicineDto
}