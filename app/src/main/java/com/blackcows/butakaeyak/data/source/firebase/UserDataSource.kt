package com.blackcows.butakaeyak.data.source.firebase

import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.toMap
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

    suspend fun getUserWithLoginId(id: String, pwd: String): Result<User> {
        val result = db.collection(USER_COLLECTION)
            .whereEqualTo(LOGIN_ID, id)
            .whereEqualTo(PASSWORD, pwd)
            .get().await()?.toObjects(User::class.java)?.getOrNull(0)

        return if(result == null) Result.failure(USER_NOT_FOUND_EXCEPTION)
                else Result.success(result)
    }

    private suspend fun isDuplicatedId(id: String): Boolean {
        val result = db.collection(USER_COLLECTION)
            .whereEqualTo(LOGIN_ID, id)
            .get().await()?.toObjects(User::class.java)?.getOrNull(0)

        return (result != null)
    }

    suspend fun getUserWithKakaoId(id: Long): Result<User> {
        val result = db.collection(USER_COLLECTION)
            .whereEqualTo(KAKAO_ID, id)
            .get().await()?.toObjects(User::class.java)?.getOrNull(0)

        return if(result == null) Result.failure(USER_NOT_FOUND_EXCEPTION)
        else Result.success(result)
    }

    suspend fun saveUser(user: User): Result<User> = coroutineScope {
        val hasAccount = async { user.loginId?.let { isDuplicatedId(user.loginId) } ?: false }
        val hasKakaoAccount = async {
            (user.kakaoId != null && getUserWithKakaoId(user.kakaoId).isSuccess)
        }

        if (!hasAccount.await() && !hasKakaoAccount.await()) {
            db.collection(USER_COLLECTION)
                .add(user.toMap()).await()

            Result.success(user)
        } else Result.failure(DUPLICATED_EXCEPTION)
    }

    //TODO: token 등록
    suspend fun registerDeviceToken(token: String) {

    }
}