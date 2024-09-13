package com.blackcows.butakaeyak.data.models

import java.time.LocalDate

data class Memo(
    val userId: String,
    val groups: MedicineGroup,
    val content: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
