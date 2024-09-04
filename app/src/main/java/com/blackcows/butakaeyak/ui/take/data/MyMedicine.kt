package com.blackcows.butakaeyak.ui.take.data

import com.blackcows.butakaeyak.data.models.Medicine
import io.ktor.util.date.WeekDay

data class MyMedicine(
    val medicine: Medicine,
    val alarms: Map<WeekDay, List<String>>
)
