package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.OCRRequest
import com.blackcows.butakaeyak.data.models.OCRResponse

interface OCRRepository {
    suspend fun createChatCompletion(request: OCRRequest): OCRResponse
}