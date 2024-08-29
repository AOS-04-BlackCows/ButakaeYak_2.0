package com.example.yactong.data.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class NaverSearchRetrofitClient {
    private val client = OkHttpClient()
    // 검색어 UTF-8로 인코딩 필수
    val query: String = "약국"
    // 한번에 표시할 검색 결과 개수
    val display: Int = 50
    // 검색 시작 위치
    val start: Int = 1
    // 정확도
    val sort: String = "sim"
    private val NAVER_BASE_URL = "https://openapi.naver.com/v1/search/local.json?query=$query&display=$display&start=$start&sort=$sort"
    private val NAVER_CLIENT_ID = "85IxIAJUAEGdxPQAgeca"
    private val NAVER_CLIENT_SECRET = "3dZ23fhqou"

    fun fetchPharmacyCoordinates () {
        val request = Request.Builder()
            .url(NAVER_BASE_URL)
            .addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
            .addHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
            .build()

        client.newCall(request).execute().use { response: Response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpeted code $response")
            }
            val resposeData = response.body?.string()
            if (!resposeData.isNullOrBlank()) {
                parsePharmacyData(resposeData)
            }
        }
    }

    private fun parsePharmacyData (data: String) {
        val jsonObject = JSONObject(data)
        val items = jsonObject.getJSONArray("items")

        Log.d("parsePharmacyData", "data = $data")
        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            val title = item.getString("title")
            val address = item.getString("address")
            val mapx = item.getString("mapx")
            val mapy = item.getString("mapy")

            Log.d("parsePharmacyData", title)
            Log.d("parsePharmacyData", address)
            Log.d("parsePharmacyData", mapx)
            Log.d("parsePharmacyData", mapy)
            Log.d("parsePharmacyDataDivider","----------------")
        }
    }
}