package com.blackcows.butakaeyak.data.models

import io.ktor.util.date.WeekDay
import java.time.LocalDate

data class Calendar(
    val userId: String,
    val yearMonth: String,      //format: "20240912"
    val diaries: List<Diary>       //format: "24:내용"
) {
    data class Diary(
        val date: LocalDate,
        val content: String
    )
}
