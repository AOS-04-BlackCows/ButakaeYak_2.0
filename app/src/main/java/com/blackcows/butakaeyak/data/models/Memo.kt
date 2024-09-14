package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose
import java.time.LocalDate

data class Memo(
    @Expose(serialize = false)
    val id: String,
    val userId: String,
    val groups: MedicineGroup,
    val content: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
