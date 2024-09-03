package com.example.yactong.firebase.firebase_store.models


// 사용자 데이터를 포함하는 객체 생성
data class UserData(
    val phoneNumber : String = "",
    val age : String = "",
    val name : String = ""
) {
    constructor() : this(", 0, ")
    fun toMap() : HashMap<String, String>{
        return hashMapOf(
            "phoneNumber" to phoneNumber,
            "age" to age.toString(),
            "name" to name
            )
    }
}
