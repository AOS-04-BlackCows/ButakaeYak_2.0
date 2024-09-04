package com.blackcows.butakaeyak.ui.take.data

import com.blackcows.butakaeyak.data.models.Medicine
import io.ktor.util.date.WeekDay
import io.ktor.util.date.WeekDay.MONDAY
import io.ktor.util.date.WeekDay.WEDNESDAY
import io.ktor.util.date.WeekDay.THURSDAY
import io.ktor.util.date.WeekDay.FRIDAY
import io.ktor.util.date.WeekDay.TUESDAY
import io.ktor.util.date.WeekDay.SATURDAY
import io.ktor.util.date.WeekDay.SUNDAY

data class MedicineAtTime(
    val time: String,
    val list: List<Medicine>
)

fun WeekDay.fromKorean(str: String): WeekDay
= when(str) {
    "월요일" -> MONDAY
    "화요일" -> TUESDAY
    "수요일" -> WEDNESDAY
    "목요일" -> THURSDAY
    "금요일" -> FRIDAY
    "토요일" -> SATURDAY
    "일요일" -> SUNDAY
    else -> throw Exception("Invalid day of week: $value")
}