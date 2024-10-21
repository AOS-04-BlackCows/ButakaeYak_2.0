package com.blackcows.butakaeyak.data.repository.impl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.source.firebase.ImageDataSource
import com.blackcows.butakaeyak.data.source.firebase.UserDataSource
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val localUtilsDataSource: LocalUtilsDataSource,
    private val imageDataSource: ImageDataSource
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun loginWithId(id: String, pwd: String): LoginResult {
        return runCatching {
            val result = userDataSource.getUserWithLoginId(id, pwd)
            if(result != null) {
                localUtilsDataSource.setSignIn(true)
                LoginResult.Success(result)
            } else LoginResult.UnknownAccount
        }.getOrDefault(LoginResult.Failure)
    }

    override suspend fun loginWithKakaoId(kakaoId: Long): LoginResult {
        return runCatching {
            val result = userDataSource.getUserWithKakaoId(kakaoId)
            if(result != null) {
                localUtilsDataSource.setSignIn(true)
                Log.d(TAG, "signIn? ${localUtilsDataSource.isSignIn()}")
                LoginResult.Success(result)
            } else LoginResult.UnknownAccount
        }.getOrDefault(LoginResult.Failure)
    }

    override suspend fun signUpUserData(userRequest: UserRequest): SignUpResult {
        return runCatching {
            val isDuplicated = userDataSource.isDuplicatedId(userRequest.loginId!!)

            if(isDuplicated) {
                SignUpResult.LoginIdDuplicate(userRequest.kakaoId!!.toString())
            } else {
                SignUpResult.Success(userDataSource.saveUser(userRequest))
            }
        }.onFailure {
            Log.w(TAG, "signUpUserData failed) msg: ${it.message}")
        }.getOrDefault(SignUpResult.Failure)
    }

    override suspend fun trySignUpWithKakao(): SignUpResult {
        return kotlin.runCatching {
            val loginResult = suspendCoroutine<UserRequest?> {
                UserApiClient.instance.me { user, error ->
                    if(user != null) {
                        it.resume(
                            UserRequest(
                                name = user.kakaoAccount!!.profile!!.nickname!!,
                                loginId = null,
                                pwd = null,
                                profileUrl = user.kakaoAccount!!.profile!!.profileImageUrl!!,
                                kakaoId = user.id!!,
                                naverId = null,
                                googleId = null,
                                deviceToken = null
                            )
                        )
                    } else {
                        it.resume(null)
                    }
                }
            }

            return if(loginResult == null) {
                SignUpResult.KakaoSignUpFail
            } else {
                //중복체크
                val isDuplicated = userDataSource.isDuplicatedId(loginResult.kakaoId!!.toString())
                if(isDuplicated) {
                    SignUpResult.LoginIdDuplicate(loginResult.kakaoId.toString())
                } else {
                    SignUpResult.Success(
                        userDataSource.saveUser(loginResult)
                    )
                }
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

        localUtilsDataSource.setSignIn(false)
        localUtilsDataSource.deleteAutoLoginData()

        return result
    }

    override suspend fun deleteAccount(user: User) {
        runCatching {
            userDataSource.deleteAccount(user)
        }.onFailure {
            Log.w(TAG, "deleteAccount failed) msg: ${it.message}")
            println("deleteAccount failed) msg: ${it.message}")
        }
    }

    override suspend fun deleteAccount(id: String) {
        runCatching {
            userDataSource.deleteAccount(id)
        }.onFailure {
            Log.w(TAG, "deleteAccount failed) msg: ${it.message}")
            println("deleteAccount failed) msg: ${it.message}")
        }
    }

    override suspend fun setProfile(user: User, bitmap: Bitmap): User {
        imageDataSource.uploadProfile(user.id, bitmap)

        val url = imageDataSource.getHttpUrl(user.id)
        val updated = user.copy(
            profileUrl = url
        )
        return userDataSource.updateUser(updated)
    }

    override suspend fun setProfile(user: User, url: String): User {
        val updated = user.copy(
            profileUrl = url
        )
        return userDataSource.updateUser(updated)
    }

    override suspend fun deleteProfile(user: User): User {
        val updated = user.copy(
            profileUrl = null
        )
        return userDataSource.updateUser(updated)
    }

    override suspend fun registerDeviceToken(user: User): User {
        val token = FirebaseMessaging.getInstance().token.await()
        val newOne = user.copy(
            deviceToken = token
        )

        Log.d("Firebase", "${user.name}'s Token is Updated: ${token}")

        return userDataSource.updateUser(newOne)
    }

    override suspend fun getProfileAndName(userId: String): ScheduleProfile {
        val imageUrl = ""//imageDataSource.getHttpUrl(userId)

        val user = userDataSource.getUserWithId(userId)!!
        val name = user.name
        val token = user.deviceToken!!

        return ScheduleProfile(
            userId, name, imageUrl, token
        )
    }
}