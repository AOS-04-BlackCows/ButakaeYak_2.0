package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSource @Inject constructor(

){
    private val db = Firebase.firestore


    companion object {
        private const val TAG = "UserSource"
        private const val USER_COLLECTION = "users"

        private const val LOGIN_ID = "loginId"
        private const val PASSWORD = "pwd"
        private const val KAKAO_ID = "kakaoId"

        val USER_NOT_FOUND_EXCEPTION = object : Exception() {
            override val message: String
                get() = "가입된 정보가 없습니다."
        }
        val DUPLICATED_EXCEPTION = object : Exception() {
            override val message: String
                get() = "이미 가입된 사용자입니다."
        }
    }

    suspend fun getUserWithLoginId(id: String, pwd: String): User? {
        return kotlin.runCatching {
            db.collection(USER_COLLECTION)
                .whereEqualTo(LOGIN_ID, id)
                .whereEqualTo(PASSWORD, pwd)
                .get().await().toObjectsWithId<User>().getOrNull(0)
        }.getOrNull()
    }

    private suspend fun isDuplicatedId(id: String): Boolean {
        return kotlin.runCatching {
            val result = db.collection(USER_COLLECTION)
                .whereEqualTo(LOGIN_ID, id)
                .get().await()?.toObjects(User::class.java)?.getOrNull(0)

            (result != null)
        }.getOrDefault(false)
    }

    suspend fun getUserWithKakaoId(id: Long): User? {
        return kotlin.runCatching {
            db.collection(USER_COLLECTION)
                .whereEqualTo(KAKAO_ID, id)
                .get().await().toObjectsWithId<User>().firstOrNull()
        }.getOrNull()
    }

    suspend fun saveUser(userRequest: UserRequest) {
        kotlin.runCatching {
            db.collection(USER_COLLECTION)
                .add(userRequest)
                .await()
        }.onFailure {
            Log.w(TAG, "saveUser Failed) msg: ${it.message}")
        }
    }

    suspend fun registerDeviceToken(user: User, token: String): User? {
        return kotlin.runCatching {
            val withToken = user.copy(
                deviceToken = token
            )

            db.collection(USER_COLLECTION)
                .document(user.id)
                .set(withToken)

            withToken
        }.getOrNull()
    }
}