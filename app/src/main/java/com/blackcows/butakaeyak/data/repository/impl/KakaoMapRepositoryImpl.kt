package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.data.repository.KakaoMapRepository
import com.blackcows.butakaeyak.data.source.api.KakaoMapSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class KakaoMapRepositoryImpl @Inject constructor(
    private val kakaoMapSource: KakaoMapSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): KakaoMapRepository {
    override fun searchCategory(x: String, y: String, callback: (List<KakaoPlace>) -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            kotlin.runCatching {
                kakaoMapSource.searchCategoryPlace(x, y)
            }.onSuccess {
                callback(it)
            }.onFailure { callback(listOf()) }
        }
    }

    override fun searchPlace(x: String,y: String, callback: (List<KakaoPlace>) -> Unit) {
        // TODO("Not yet implemented")
    }
}