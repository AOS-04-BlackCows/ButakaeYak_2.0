package com.example.yactong.data.dto

import android.os.Parcelable
import com.example.yactong.data.models.Drug
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrugInfoDto(
    val header: DrugHeader,
    val body: DrugBody
): Parcelable

@Parcelize
data class DrugHeader(
    val resultCode: String,
    val resultMsg: String,
): Parcelable

@Parcelize
data class DrugBody(
    val pageNo: Int,
    val totalCount: Int,
    val numOfRows: Int,
    val items: List<DrugItem>
): Parcelable

@Parcelize
data class DrugItem(
    val entpName: String,
    val itemName: String,
    val itemSeq: String,
    val efcyQesitm: String,
    val useMethodQesitm: String,
    val atpnWarnQesitm: String,
    val atpnQesitm: String,
    val seQesitm: String,
    val depositMethodQesitm: String,
    val openDe: String,
    val updateDe: String,
    val itemImage: String?
): Parcelable {
    fun toDrug() = Drug(
        name = itemName
    )
}
