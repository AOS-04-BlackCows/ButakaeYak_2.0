package com.blackcows.butakaeyak.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.data.repository.DrugRepository
import com.blackcows.butakaeyak.data.repository.KakaoMapRepository
import com.blackcows.butakaeyak.data.repository.PharmacyInfoRepository
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.KakaoInterceptor
import com.blackcows.butakaeyak.data.retrofit.PharmacyInfoApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.retrofit.RetrofitClientXml
import dagger.hilt.android.lifecycle.HiltViewModel
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.create
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "k3f_MapViewModel"

@HiltViewModel
class MapViewModel @Inject constructor(
    private val kakaoRepository: KakaoMapRepository
) : ViewModel() {

    private var pharmacyInfoRepository: PharmacyInfoRepository? = null

    private val _items = MutableLiveData<List<KakaoPlace>>()
    val items: LiveData<List<KakaoPlace>>
        get() = _items

//    fun apiPharmacyInfoList() = runBlocking {
//        val result = suspendCoroutine { continuation ->
//            pharmacyInfoRepository?.searchPharmacyInfo("성남시 분당구") { drugs ->
//                // callback 내용 작성
//                continuation.resume(drugs)
//            }
//        }
//
//        assertNotNull(result)
//        assertTrue(result.isNotEmpty())
//
//        result.forEachIndexed { i, it ->
//            Log.d(TAG,"$i: ${it.dutyName}")
//        }
//    }

    fun apiPharmacyInfoList() = runBlocking {
        val instance = RetrofitClientXml.getInstance(ApiBaseUrl.PharmacyListInfoUrl)
        val retrofit = instance.create(PharmacyInfoApiService::class.java)
        Log.d(TAG, "retrofit")
        val result = retrofit.getPharmacyInfo("경기도 성남시", null, null, null, null)

        Log.d(TAG, "result")
        result.body.items?.forEachIndexed { i, it ->
            Log.d(TAG, "$i: ${it.dutyName}, ${it}")
        }

        Log.d(
            TAG,
            "resultCode: ${result.header.resultCode}, item: ${result.body.items?.get(0)?.dutyAddr}"
        )
    }

    fun communicateNetWork(x: String, y: String) {
        kakaoRepository.searchPlace(x, y) { kakaoPlaces ->
            kakaoPlaces.forEachIndexed { i, it ->
                Log.d(TAG, "x = $x, y = $y :$i: ${it}")
            }
            _items.value = kakaoPlaces
        }
        Log.d(TAG, "$items")
    }
}