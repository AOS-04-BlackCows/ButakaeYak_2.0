package com.blackcows.butakaeyak.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.KakaoInterceptor
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.create
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    private val _items = MutableLiveData<List<KakaoPlace>>()
    val items: LiveData<List<KakaoPlace>>
        get() = _items

    fun communicateNetWork(param: String) {
        viewModelScope.launch {
            val instance = RetrofitClient.getInstance(ApiBaseUrl.KakaoPlaceSearchUrl)
            val retrofit = instance.create(KakaoApiService::class.java)
            val result = retrofit.getCategoryInfo("126.9795003", "37.5668114")
            result.documents.forEachIndexed {i, it ->
                Log.d("제발나와주세요 카카오맵정보님","$i: ${it}")
            }
        }
    }
}