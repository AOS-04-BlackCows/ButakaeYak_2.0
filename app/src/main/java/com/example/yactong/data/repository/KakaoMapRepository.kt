package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.KakaoPlace
import com.example.yactong.data.models.Pill

interface KakaoMapRepository {
    fun searchCategory(x: String,y: String, callback: (List<KakaoPlace>) -> (Unit))
    fun searchPlace(x: String,y: String, callback: (List<KakaoPlace>) -> (Unit))
}