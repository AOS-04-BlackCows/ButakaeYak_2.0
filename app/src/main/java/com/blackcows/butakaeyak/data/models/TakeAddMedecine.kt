package com.blackcows.butakaeyak.data.models

data class TakeAddMedicine(
    var imageUrl: String = "medicine_type_1",
    var name: String?,
    val isDetail: Boolean? = false
)
