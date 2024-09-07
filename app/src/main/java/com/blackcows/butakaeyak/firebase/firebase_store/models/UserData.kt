package com.blackcows.butakaeyak.firebase.firebase_store.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// 사용자 데이터를 포함하는 객체 생성
@Parcelize
data class UserData(
    val name : String = "",
    val id : String = "",
    val thumbnail : String = "",
    val pwd : String = "",
    val kakaoId: Long = 0L
) : Parcelable {
    // 이미 확장함수로 정의해놨음. -> data/mapper.kt
//    fun toMap() : HashMap<String, String>{
//        return hashMapOf(
//            "name" to name,
//            "id" to id,
//            "thumbnail" to thumbnail,
//            "pwd" to pwd
//        )
//    }
}
