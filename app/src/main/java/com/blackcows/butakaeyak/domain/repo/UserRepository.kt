package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData

interface UserRepository {
    suspend fun getUserWithLoginId(id: String): Result<UserData>
    suspend fun getUserWithKakaoId(kakaoId: Long): Result<UserData>
    suspend fun signUpUserData(userData: UserData): Result<UserData>
}