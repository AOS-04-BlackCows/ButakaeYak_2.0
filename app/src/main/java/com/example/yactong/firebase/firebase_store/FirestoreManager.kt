package com.example.yactong.firebase.firebase_store

import android.util.Log
import com.example.yactong.firebase.firebase_store.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// db. 넣기
class FirestoreManager {

    private val TAG : String = "FirestoreManager"

    // firestore 인스턴스 가져오기
    private val db = Firebase.firestore

    fun trySignUp(userData: UserData, resultListener: ResultListener) {
        // 컬렉션에 문서 추가 (아이디 자동 생성)
        db.collection("users")  // 자동 아이디 넣기
            .add(userData.toMap()) // setOptions.merge -> 덮어쓰기 XX
            .addOnSuccessListener { resultListener.onSuccess() }
            .addOnFailureListener { resultListener.onFailure(it) }
    }

    // Firebase에서 전화번호로 사용자 검색
    // 사용자가 입력한 phoneNumber과 콜백 인터페이스를 매개변수로 받음
    fun trySignIn(phoneNumber: String, resultListener: ResultListener) {
        db.collection("users")
            // 사용자와 일치하는 사용자를 찾는다.
            .whereEqualTo("phoneNumber", phoneNumber)
            // 이해 못함 -> 대충 결과를 가져오는 메서드임
            .get()
            .addOnSuccessListener { documents -> // 반환하는 문서에서
                if (documents.isEmpty){ // 일치하는 번호(사용자)가 없으면
                    resultListener.onFailure(java.lang.Exception("사용자 정보를 찾을 수 없습니다!")) //실패 콜백 함수를 불러 찾을 수 없다고 알림
                } else {
                    resultListener.onSuccess() // 있으면 성공~
                }
            }
            .addOnFailureListener{ resultListener.onFailure(it) }
    }



    // 로그인 성공 or 실패 시 호출되는 콜백! 인터페이스
    interface ResultListener {
        fun <T> onSuccess(result : T)
        //  fun <T> onSuccess(result : T)
        fun onFailure(e: Exception)
    }
}