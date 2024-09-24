package com.blackcows.butakaeyak.ui.schedule

import com.blackcows.butakaeyak.data.models.MedicineGroup
import java.time.LocalDate

object ScheduleMockData {
    val group1 = MedicineGroup(
        id = "vulputate",
        name = "group1",
        userId = "phasellus",
        medicines = listOf(),
        customNameList = listOf(),
        imageUrlList = listOf(),
        startedAt = LocalDate.parse("2024-05-15"),
        finishedAt = LocalDate.parse("2100-05-15"),
        daysOfWeeks = listOf(),
        alarms = listOf("08:00", "12:00", "18:00"),
        hasTaken = listOf("2024-09-24 08:00", "2024-09-24 12:00")
    )
    val group2 = MedicineGroup(
        id = "aliquip",
        name = "group2",
        userId = "aenean",
        medicines = listOf(),
        customNameList = listOf(),
        imageUrlList = listOf(),
        startedAt = LocalDate.parse("2024-05-15"),
        finishedAt = LocalDate.parse("2100-05-15"),
        daysOfWeeks = listOf(),
        alarms = listOf("08:00", "12:00", "18:00"),
        hasTaken = listOf("2024-09-24 08:00", "2024-09-24 12:00")
    )
    val group3 = MedicineGroup(
        id = "mlfae;",
        name = "group3",
        userId = "aenean",
        medicines = listOf(),
        customNameList = listOf(),
        imageUrlList = listOf(),
        startedAt = LocalDate.parse("2024-05-15"),
        finishedAt = LocalDate.parse("2100-05-15"),
        daysOfWeeks = listOf(),
        alarms = listOf("08:00", "12:00", "18:00"),
        hasTaken = listOf("2024-09-24 08:00")
    )

    val medicineGroups = listOf(group1, group2, group3)
}