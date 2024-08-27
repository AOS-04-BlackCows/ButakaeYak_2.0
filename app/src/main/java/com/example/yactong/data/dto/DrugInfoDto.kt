package com.example.yactong.data.dto

import java.time.LocalDate

data class DrugInfoDto(
    val header: Header,
    val body: Body
)

data class Header(
    val resultCode: String,
    val resultMsg: String,
)

data class Body(
    val pageNo: Int,
    val totalCount: Int,
    val numOfRows: Int,
    val items: List<Item>
)

data class Item(
    val entpName: String,
    val itemName: String,
    val itemSeq: String,
    val efcyQesitm: String,
    val useMethodQesitm: String,
    val atpnWarnQesitm: String,
    val atpnQesitm: String,
    val seQesitm: String,
    val depositMethodQesitm: String,
    val openDe: LocalDate,
    val updateDe: LocalDate,
    val itemImage: String?
)
