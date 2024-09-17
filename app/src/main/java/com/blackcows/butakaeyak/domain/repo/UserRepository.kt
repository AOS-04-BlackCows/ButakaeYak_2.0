package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult

interface UserRepository {
    suspend fun loginWithId(id: String, pwd: String): LoginResult
    suspend fun loginWithKakaoId(kakaoId: Long): LoginResult
    suspend fun signUpUserData(userData: User): SignUpResult
    suspend fun trySignUpWithKakao(): SignUpResult
    suspend fun logout(): Boolean
    suspend fun deleteAccount(user: User)
}