package com.blackcows.butakaeyak.ui.take.data

import com.blackcows.butakaeyak.data.models.Medicine

data class MedicineAtTime(
    val time: String,
    val list: List<Medicine>
)