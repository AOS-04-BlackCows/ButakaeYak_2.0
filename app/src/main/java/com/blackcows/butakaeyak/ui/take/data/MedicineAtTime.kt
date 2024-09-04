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

/**
 * @property weekDay: 먹어야 하는 요일
 * @property time: 복용해야 하는 시간
 * @property list: 시간에 맞춰 먹어야 할 약들
 */
data class MedicineAtTime(
    val weekDay: WeekDay,
    val time: String,
    val list: List<Medicine>
)

