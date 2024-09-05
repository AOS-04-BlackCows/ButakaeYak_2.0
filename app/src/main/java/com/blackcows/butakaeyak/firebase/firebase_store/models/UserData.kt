package com.blackcows.butakaeyak.firebase.firebase_store.models


// 사용자 데이터를 포함하는 객체 생성
data class UserData(
    val name : String = "",
    val id : String = "",
) {
    constructor() : this("","")
    fun toMap() : HashMap<String, String>{
        return hashMapOf(
            "name" to name,
            "id" to id,
            )
    }
}
