package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.data.models.Pill

interface KakaoMapRepository {
    suspend fun searchCategory(x: String,y: String): List<KakaoPlace>
    fun searchPlace(x: String,y: String, callback: (List<KakaoPlace>) -> (Unit))
}