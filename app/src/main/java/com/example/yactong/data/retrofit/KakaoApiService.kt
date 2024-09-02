package com.example.yactong.data.retrofit

import com.example.yactong.BuildConfig
import com.example.yactong.data.dto.KakaoMapDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("category.json")
    suspend fun getCategoryInfo(
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("radius") radius: Int = 1000,
        @Query("sort") sort: String = "distance",
        @Query("category_group_code") category_group_code: String = "PM9"
    ): KakaoMapDTO

    @GET("keyword.json")
    suspend fun getKeywordInfo(
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("size") size: Int,
        @Query("radius") radius: Int = 1000,
        @Query("sort") sort: String = "distance",
        @Query("category_group_code") category_group_code: String = "PM9"
    ): KakaoMapDTO
}