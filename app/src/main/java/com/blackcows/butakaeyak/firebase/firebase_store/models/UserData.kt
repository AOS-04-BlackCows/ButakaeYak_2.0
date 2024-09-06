package com.blackcows.butakaeyak.firebase.firebase_store.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// 사용자 데이터를 포함하는 객체 생성
@Parcelize
data class UserData(
    val name : String = "",
    val id : String = "",
    val thumbnail : String = "",
    val pwd : String = ""
) : Parcelable {
    fun toMap() : HashMap<String, String>{
        return hashMapOf(
            "name" to name,
            "id" to id,
            "thumbnail" to thumbnail,
            "pwd" to pwd
        )
    }
}
