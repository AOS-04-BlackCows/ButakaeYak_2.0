package com.blackcows.butakaeyak.data.models

import com.tickaroo.tikxml.annotation.PropertyElement

data class MedicineDetail(
    val name: String,
    val entpName: String,
    val shape: String,
    val effect: String,
    val instruction: String,
    val caution: String,
    val storing: String,
)