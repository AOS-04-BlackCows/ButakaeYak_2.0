package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.OCRRequest
import com.blackcows.butakaeyak.data.models.OCRResponse
import com.blackcows.butakaeyak.data.retrofit.service.OCRTextRecApiService
import com.blackcows.butakaeyak.domain.repo.OCRRepository
import javax.inject.Inject

class OCRRepositoryImpl @Inject constructor(
 private val ocrService : OCRTextRecApiService
): OCRRepository{
    override suspend fun createChatCompletion(request: OCRRequest): OCRResponse {
        return ocrService.createChatCompletion(request)
    }
}