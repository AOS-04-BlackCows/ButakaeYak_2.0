package com.blackcows.butakaeyak.data.models

import io.ktor.util.date.WeekDay

data class Calendar(
    val userId: String,
    val yearMonth: String,      //format: "20240912"
    val diary: String       //format: "24:내용"
)
