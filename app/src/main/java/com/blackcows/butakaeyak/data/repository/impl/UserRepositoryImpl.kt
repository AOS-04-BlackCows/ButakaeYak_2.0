package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.source.firebase.UserDataSource
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUserWithLoginId(id: String): Result<UserData> {
        return userDataSource.getUserWithLoginId(id)
    }

    override suspend fun getUserWithKakaoId(kakaoId: Long): Result<UserData> {
        return userDataSource.getUserWithKakaoId(kakaoId)
    }

    override suspend fun signUpUserData(userData: UserData): Result<UserData> {
        return userDataSource.saveUserData(userData)
    }
}