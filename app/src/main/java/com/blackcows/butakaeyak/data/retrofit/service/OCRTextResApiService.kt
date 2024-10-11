package com.blackcows.butakaeyak.data.retrofit.service

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.models.OCRRequest
import com.blackcows.butakaeyak.data.models.OCRResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val API_KEY = BuildConfig.OPEN_AI_KEY
const val AI_MODEL : String = "gpt-4o-mini" //gpt-4o-mini   //gpt-3.5-turbo
interface OCRTextRecApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer $API_KEY"
    )
    @POST("chat/completions")
    suspend fun createChatCompletion(@Body request: OCRRequest): OCRResponse
}