package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.source.firebase.UserDataSource
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager.ResultListener
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {



    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun loginWithId(id: String, pwd: String): Result<UserData> {
        return userDataSource.getUserWithLoginId(id, pwd)
    }

    override suspend fun loginWithKakaoId(kakaoId: Long): Result<UserData> {
        return userDataSource.getUserWithKakaoId(kakaoId)
    }

    override suspend fun signUpUserData(userData: UserData): Result<UserData> {
        return userDataSource.saveUserData(userData)
    }

    override suspend fun trySignInWithKakao(): Result<UserData> {
        val result = suspendCoroutine {
            UserApiClient.instance.me{ user, error ->
                if (error!= null) {
                    Log.e(TAG, "카카오 사용자 정보 요청 실패: $error")
                    it.resume(null)
                } else if (user != null) {
                    Log.i(TAG, "사용자 정보 요청 성공 ${user.id}")

                    val userData = UserData(
                        name = user.kakaoAccount?.profile?.nickname !!,
                        kakaoId = user.id!!,
                        thumbnail = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                    )

                    it.resume(userData)
                }
            }
        }

        return if(result != null) {
            val savedUserData = userDataSource.getUserWithKakaoId(result.kakaoId!!).getOrNull()
            Log.d(TAG, "savedUserData is null? ${savedUserData == null}")
            if(savedUserData == null) {
                userDataSource.saveUserData(result)
            } else Result.success(savedUserData)

        } else {
            Result.failure(Exception("카카오 로그인을 실패하였습니다."))
        }

    }

    override  suspend fun logoutKakao(): Boolean {
        val result = suspendCoroutine {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    it.resume(false)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    it.resume(true)
                }
            }
        }

        return result

    }
}