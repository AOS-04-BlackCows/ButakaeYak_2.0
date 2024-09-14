package com.blackcows.butakaeyak.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @Expose(serialize = false)
    val id: String,
    val name: String,
    val loginId: String?,
    val pwd: String?,
    val profileUrl: String?,
    val kakaoId: Long?,
    val deviceToken: String?
): Parcelable


data class UserRequest(
    val name: String,
    val loginId: String?,
    val pwd: String?,
    val profileUrl: String?,
    val kakaoId: Long?,
    val deviceToken: String?
)
