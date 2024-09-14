package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose
import io.ktor.util.date.WeekDay
import java.time.LocalDate

data class MedicineGroup (
    @Expose(serialize = false)
    val id: String,
    val name: String,
    val userId: String,
    val medicines: List<MedicineDetail>,
    val customNameList: List<String>,
    val memos: List<Memo>,
    val startedAt: LocalDate,
    val finishedAt: LocalDate,
    val daysOfWeeks: List<WeekDay>,
    val alarms: List<String>        //format: "10:30", "12:40"
)

data class MedicineGroupResponse(
    val id: String,
    val name: String,
    val userId: String,
    val medicines: List<String>,
    val customNameList: List<String>,
    val memos: List<String>,
    val startedAt: String,
    val finishedAt: String,
    val daysOfWeeks: List<String>,
    val alarms: List<String>
)

data class MedicineGroupRequest(
    val name: String,
    val userId: String,
    val medicines: List<String>,
    val customNameList: List<String>,
    val memos: List<String>,
    val startedAt: String,
    val finishedAt: String,
    val daysOfWeeks: List<String>,
    val alarms: List<String>
)