package com.blackcows.butakaeyak.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val loginId: String?,
    val pwd: String?,
    val profileUrl: String?,
    val kakaoId: Long?,
    val deviceToken: String?
): Parcelable
