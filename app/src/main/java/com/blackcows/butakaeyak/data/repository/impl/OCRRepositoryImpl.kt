package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.OCRRequest
import com.blackcows.butakaeyak.data.models.OCRResponse
import com.blackcows.butakaeyak.data.retrofit.OCRRetrofitClient
import com.blackcows.butakaeyak.domain.repo.OCRRepository
import javax.inject.Inject

class OCRRepositoryImpl @Inject constructor(

): OCRRepository{
    override suspend fun createChatCompletion(request: OCRRequest): OCRResponse {
        return OCRRetrofitClient.openAiApi.createChatCompletion(request)
    }
}