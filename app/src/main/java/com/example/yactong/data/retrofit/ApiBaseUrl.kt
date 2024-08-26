package com.example.yactong.data.retrofit

sealed class ApiBaseUrl(val url: String) {
    data object DrugInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService")
    data object PillInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01")
}