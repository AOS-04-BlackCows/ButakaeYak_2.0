package com.blackcows.butakaeyak.data.dto

import android.os.Parcelable
import com.blackcows.butakaeyak.data.models.PharmacyInfo
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.parcelize.Parcelize

@Xml(name = "response")
data class PharmacyListInfoDTO(
    @Element(name = "header") val header: PharmacyHeader,
    @Element(name = "body") val body: PharmacyBody
)

@Xml(name = "header")
data class PharmacyHeader(
    @PropertyElement(name = "resultCode") val resultCode: String,
    @PropertyElement(name = "resultMsg") val resultMsg: String
)

@Xml(name = "body")
data class PharmacyBody(
    @Element(name = "items") val items: List<PharmacyItem>?,
    @PropertyElement(name = "numOfRows") val numOfRows: Int = 0,
    @PropertyElement(name = "pageNo") val pageNo: Int = 0,
    @PropertyElement(name = "totalCount") val totalCount: Int = 0
)

@Xml(name = "item")
@Parcelize
data class PharmacyItem(
    @PropertyElement(name = "dutyAddr") val dutyAddr: String?,
    @PropertyElement(name = "dutyEtc") val dutyEtc: String?,
    @PropertyElement(name = "dutyMapimg") val dutyMapimg: String?,
    @PropertyElement(name = "dutyName") val dutyName: String?,
    @PropertyElement(name = "dutyTel1") val dutyTel1: String?,
    @PropertyElement(name = "dutyTime1c") val dutyTime1c: String?,
    @PropertyElement(name = "dutyTime2c") val dutyTime2c: String?,
    @PropertyElement(name = "dutyTime3c") val dutyTime3c: String?,
    @PropertyElement(name = "dutyTime4c") val dutyTime4c: String?,
    @PropertyElement(name = "dutyTime5c") val dutyTime5c: String?,
    @PropertyElement(name = "dutyTime6c") val dutyTime6c: String?,
    @PropertyElement(name = "dutyTime7c") val dutyTime7c: String?,
    @PropertyElement(name = "dutyTime8c") val dutyTime8c: String?,
    @PropertyElement(name = "dutyTime1s") val dutyTime1s: String?,
    @PropertyElement(name = "dutyTime2s") val dutyTime2s: String?,
    @PropertyElement(name = "dutyTime3s") val dutyTime3s: String?,
    @PropertyElement(name = "dutyTime4s") val dutyTime4s: String?,
    @PropertyElement(name = "dutyTime5s") val dutyTime5s: String?,
    @PropertyElement(name = "dutyTime6s") val dutyTime6s: String?,
    @PropertyElement(name = "dutyTime7s") val dutyTime7s: String?,
    @PropertyElement(name = "dutyTime8s") val dutyTime8s: String?,
    @PropertyElement(name = "hpid") val hpid: String?,
    @PropertyElement(name = "postCdn1") val postCdn1: String?,
    @PropertyElement(name = "postCdn2") val postCdn2: String?,
    @PropertyElement(name = "rnum") val rnum: String?,
    @PropertyElement(name = "wgs84Lon") val wgs84Lon: String?,
    @PropertyElement(name = "wgs84Lat") val wgs84Lat: String?
): Parcelable {
    fun toPharmacyInfo() = PharmacyInfo(
        dutyAddr = dutyAddr?: "",
        dutyEtc = dutyEtc?: "",
        dutyMapimg = dutyMapimg?: "",
        dutyName = dutyName?: "",
        dutyTel1 = dutyTel1?: "",
        dutyTime1c = dutyTime1c?: "",
        dutyTime2c = dutyTime2c?: "",
        dutyTime3c = dutyTime3c?: "",
        dutyTime4c = dutyTime4c?: "",
        dutyTime5c = dutyTime5c?: "",
        dutyTime6c = dutyTime6c?: "",
        dutyTime7c = dutyTime7c?: "",
        dutyTime8c = dutyTime8c?: "",
        dutyTime1s = dutyTime1s?: "",
        dutyTime2s = dutyTime2s?: "",
        dutyTime3s = dutyTime3s?: "",
        dutyTime4s = dutyTime4s?: "",
        dutyTime5s = dutyTime5s?: "",
        dutyTime6s = dutyTime6s?: "",
        dutyTime7s = dutyTime7s?: "",
        dutyTime8s = dutyTime8s?: "",
        hpid = hpid?: "",
        postCdn1 = postCdn1?: "",
        postCdn2 = postCdn2?: "",
        rnum = rnum?: "",
        wgs84Lon = wgs84Lon?: "",
        wgs84Lat = wgs84Lat?: ""
    )
}