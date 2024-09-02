package com.example.yactong.data.source.api

import android.util.Log
import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.KakaoPlace
import com.example.yactong.data.retrofit.KakaoApiService
import javax.inject.Inject

class KakaoMapSource @Inject constructor(
    private val retrofit: KakaoApiService
) {
    private val tag = "DrugDataSource"

    suspend fun searchCategoryPlace(x: String,y: String): List<KakaoPlace> {
        val list = mutableListOf<KakaoPlace>()
        val result = retrofit.getCategoryInfo(x, y)
        result.documents.forEach {
            list.add(it.toKakaoPlace())
        }
// 대 승모의 오류를 처리하며 작성하던 코드
//        if(result.header.resultCode == "00") {
//            result.body.items.forEach {
//                list.add(it.toKakaoPlace())
//            }
//        } else {
//            Log.d(tag, "SearchDrugs(code: ${result.header.resultCode}): ${result.header.resultMsg}")
//        }

        return list
    }
}