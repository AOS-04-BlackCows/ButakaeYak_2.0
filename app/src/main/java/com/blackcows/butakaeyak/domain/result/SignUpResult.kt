package com.blackcows.butakaeyak.domain.result

import com.blackcows.butakaeyak.data.models.User

sealed class SignUpResult {
    data class Success(val user: User) : SignUpResult()
    data class LoginIdDuplicate(val kakaoId: String) : SignUpResult()
    data object  KakaoSignUpFail: SignUpResult()
    data object Failure : SignUpResult()
}