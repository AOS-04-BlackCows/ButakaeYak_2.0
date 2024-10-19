package com.blackcows.butakaeyak.data.retrofit.service

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.dto.DrugInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FcmService {
    @GET("messages:send")
    suspend fun sendMessage(): DrugInfoDto
}