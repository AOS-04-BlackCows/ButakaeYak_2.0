package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.source.firebase.UserDataSource
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val localUtilsDataSource: LocalUtilsDataSource
) : UserRepository {



    companion object {
        private const val TAG = "UserRepositoryImpl"


    }

    override suspend fun loginWithId(id: String, pwd: String): LoginResult {
        return runCatching {
            val result = userDataSource.getUserWithLoginId(id, pwd)
            if(result != null) {
                LoginResult.Success(result)
            } else LoginResult.UnknownAccount
        }.getOrDefault(LoginResult.Failure)
    }

    override suspend fun loginWithKakaoId(kakaoId: Long): LoginResult {
        return runCatching {
            val result = userDataSource.getUserWithKakaoId(kakaoId)
            if(result != null) {
                LoginResult.Success(result)
            } else LoginResult.UnknownAccount
        }.getOrDefault(LoginResult.Failure)
    }

    override suspend fun signUpUserData(userData: User): SignUpResult {
        return runCatching {
            val isDuplicated = userDataSource.isDuplicatedId(userData.loginId!!)

            if(isDuplicated) {
                SignUpResult.LoginIdDuplicate
            } else {
                SignUpResult.Success(userDataSource.saveUser(userData))
            }
        }.onFailure {
            Log.w(TAG, "signUpUserData failed) msg: ${it.message}")
        }.getOrDefault(SignUpResult.Failure)
    }

    override suspend fun trySignUpWithKakao(): SignUpResult {
        return kotlin.runCatching {
            val result = suspendCoroutine {
                UserApiClient.instance.me{ user, error ->
                    if (error!= null) {
                        Log.e(TAG, "카카오 사용자 정보 요청 실패: $error")
                        it.resume(null)
                    } else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공 ${user.id}")

                        val kakaoUserRequest = UserRequest(
                            name = user.kakaoAccount?.profile?.nickname !!,
                            kakaoId = user.id!!,
                            profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                        )

                        it.resume(kakaoUserRequest)
                    }
                }
            }

            if(result == null) {
                SignUpResult.KakaoSignUpFail
            } else {
                SignUpResult.Success(
                    userDataSource.saveUser(result)
                )
            }
        }.onFailure {
            Log.w(TAG, "trySignUpWithKakao failed) msg: ${it.message}")
        }.getOrDefault(SignUpResult.Failure)
    }

    override  suspend fun logout(): Boolean {
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

        localUtilsDataSource.deleteAutoLoginData()

        return result
    }

    override suspend fun deleteAccount(user: User) {
        runCatching {
            userDataSource.deleteAccount(user)
        }.onFailure {
            Log.w(TAG, "deleteAccount failed) msg: ${it.message}")
        }
    }
}