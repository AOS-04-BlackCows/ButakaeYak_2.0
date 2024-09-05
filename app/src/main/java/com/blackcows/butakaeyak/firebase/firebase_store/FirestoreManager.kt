package com.blackcows.butakaeyak.firebase.firebase_store

import android.util.Log
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore
import com.kakao.sdk.user.UserApiClient

// db. 넣기
class FirestoreManager {
    private val TAG: String = "FirestoreManager"

    // firestore 인스턴스 가져오기
    private val db = Firebase.firestore

    fun trySignUp(userData: UserData, resultListener: ResultListener<Boolean>) {
        // 컬렉션에 문서 추가 (아이디 자동 생성)
        db.collection("users")  // 자동 아이디 넣기
            .whereEqualTo("id", userData.id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    // 이미 같은 id로 가입된 사용자가 있음
                    Log.d(TAG, "이미 가입된 아이디입니다: ${userData.id}")
                    resultListener.onFailure(Exception("이미 가입된 아이디입니다."))
                } else {
                    // 중복된 id가 없을 경우 회원가입 진행
                    db.collection("users")
                        .add(userData.toMap())
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "회원가입 성공! 문서 ID : ${documentReference.id}")
                            resultListener.onSuccess(true)
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "회원가입 실패!!!", e)
                            resultListener.onFailure(e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "id 중복체크 실패!!, e")
                resultListener.onFailure(e)
            }
    }

    // Firebase에서 id로 사용자 검색
    // 사용자가 입력한 id과 콜백 인터페이스를 매개변수로 받음
    fun trySignIn(id: String, resultListener: ResultListener<UserData>) {
        db.collection("users")
            // 사용자와 일치하는 사용자를 찾는다.
            .whereEqualTo("id", id)
            // 이해 못함 -> 대충 결과를 가져오는 메서드임
            .get()
            .addOnSuccessListener { documents -> // 반환하는 문서에서
                if (documents.isEmpty) { // 일치하는 id(사용자)가 없으면
                    Log.d(TAG, "사용자 정보를 찾을 수 없다. id : ${id}")
                    resultListener.onFailure(java.lang.Exception("사용자 정보를 찾을 수 없습니다!")) //실패 콜백 함수를 불러 찾을 수 없다고 알림
                } else {
                    // documents 안에서 사용자 찾기 구문
                    for (documents in documents) {
                        // toObject : Firestore 문서의 데이터를 지정된 클래스 타입의 객체로 전화해줌
                        val userData =
                            documents.toObject(UserData::class.java) // 변환하고자 하는 클래스 타입을 UserData로 지정
                        Log.d(TAG, "로그인 성공!! 사용자 이름 : ${userData.name}")
                        resultListener.onSuccess(userData) // 있으면 성공~
                        break
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "사용자 검색 실패!!!, e")
                resultListener.onFailure(e)
            }
    }

    fun saveKakaoUser(resultListener: ResultListener<Boolean>){
        UserApiClient.instance.me{user, error ->
            if (error!= null) {
                Log.e(TAG, "사용자 정보 요청 실패 : ${error}")
                resultListener.onFailure(Exception("카카오 사용자 정보 요청 실패"))
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성고 ${user.kakaoAccount?.email}")

                val userData = UserData(
                    id = user.id.toString(),
                    name = user.kakaoAccount?.profile?.nickname ?:""
                )

                trySignUp(userData,object : ResultListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        Log.d(TAG, "Firebase 저장 성공")
                        resultListener.onSuccess(true)
                    }

                    override fun onFailure(e: Exception) {
                        Log.e(TAG, "Firebase 저장 실패", e)
                        resultListener.onFailure(e)
                    }

                })
            }
        }
    }


    // 로그인 성공 or 실패 시 호출되는 콜백! 인터페이스
    interface ResultListener<T> {  // 제네릭 T -> 타입을 정의하는 것, 어떤 타입도 될 수 있는 임의의 타입을 의미
        fun onSuccess(result: T)

        //  fun <T> onSuccess(result : T)
        fun onFailure(e: Exception)
    }
}