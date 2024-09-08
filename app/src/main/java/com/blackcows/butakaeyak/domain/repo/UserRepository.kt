package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData

interface UserRepository {
    suspend fun loginWithId(id: String, pwd: String): Result<UserData>
    suspend fun loginWithKakaoId(kakaoId: Long): Result<UserData>
    suspend fun signUpUserData(userData: UserData): Result<UserData>
    suspend fun trySignInWithKakao(): Result<UserData>
    suspend fun logoutKakao(): Boolean
}