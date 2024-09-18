package com.blackcows.butakaeyak.data.models

import com.blackcows.butakaeyak.data.toKorean
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
) {
    fun toRequest() = MedicineGroupRequest(
        name = name,
        userId = userId,
        medicineIdList = medicines.map { it.id },
        customNameList = customNameList,
        memos = memos.map { it.id },
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        daysOfWeeks = daysOfWeeks.map { it.toKorean() },
        alarms = alarms
    )
    fun toResponse() = MedicineGroupResponse(
        id = id,
        name = name,
        userId = userId,
        medicines = medicines.map { it.id },
        customNameList = customNameList,
        memos = memos.map { it.id },
        startedAt = startedAt.toString(),
        finishedAt = finishedAt.toString(),
        daysOfWeeks = daysOfWeeks.map { it.toKorean() },
        alarms = alarms
    )
}

data class MedicineGroupRequest(
    val name: String,
    val userId: String,
    val medicineIdList: List<String>,
    val customNameList: List<String>,
    val memos: List<String>,
    val startedAt: String,
    val finishedAt: String,
    val daysOfWeeks: List<String>,
    val alarms: List<String>
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