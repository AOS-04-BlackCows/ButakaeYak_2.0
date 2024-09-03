package com.example.yactong.data.retrofit

import com.example.yactong.BuildConfig

sealed class ApiBaseUrl(val url: String) {
    data object DrugInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/")
    data object KakaoPlaceSearchUrl: ApiBaseUrl("https://dapi.kakao.com/v2/local/search/")
    data object MedicineUrl: ApiBaseUrl("https://${BuildConfig.ALGORIA_APP_ID}.algolia.net")
}