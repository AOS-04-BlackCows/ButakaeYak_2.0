package com.blackcows.butakaeyak.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MedicineDto(
    @SerializedName("header")
    val header: MedicineDtoHeader,
    @SerializedName("body")
    val body: MedicineDtoBody
): Parcelable


@Parcelize
data class MedicineDtoHeader(
    @SerializedName("resultCode")
    val code: String,
    @SerializedName("resultMsg")
    val msg: String
): Parcelable

@Parcelize
data class MedicineDtoBody(
    @SerializedName("numOfRows")
    val rows: String,

    @SerializedName("pageNo")
    val page: String,
    @SerializedName("totalCount")
    val totalCount: String,

    @SerializedName("items")
    val items: List<MedicineDtoItem>
): Parcelable


@Parcelize
data class MedicineDtoItem(
    @SerializedName("ITEM_SEQ")
    val itemSeq: String? = null,

    @SerializedName( "ITEM_NAME")
    val itemName: String? = null,

    @SerializedName("ENTP_NAME")
    val entpName: String? = null,

    @SerializedName("CHART")
    val chart: String? = null,

    @SerializedName("STORAGE_METHOD")
    val storageMethod: String? = null,

    @SerializedName("EE_DOC_DATA")
    val eeDocData: String,

    @SerializedName("UD_DOC_DATA")
    val udDocData: String,

    @SerializedName("NB_DOC_DATA")
    val nbDocData: String,
): Parcelable


//@Xml(name = "response")
//data class MedicineDto(
//    @Element(name="header")
//    val header: MedicineDtoHeader,
//    @Element(name="body")
//    val body: MedicineDtoBody
//)
//
//
//@Xml(name = "header")
//data class MedicineDtoHeader(
//    @PropertyElement(name = "resultCode")
//    val code: String,
//    @PropertyElement(name = "resultMsg")
//    val msg: String
//)
//
//@Xml(name = "body")
//data class MedicineDtoBody(
//    @PropertyElement(name = "numOfRows")
//    val rows: String,
//
//    @PropertyElement(name = "pageNo")
//    val page: String,
//    @PropertyElement(name = "totalCount")
//    val totalCount: String,
//
//    @Element(name = "items")
//    val items: MedicineItems? = null
//)
//
//@Xml(name = "items")
//data class MedicineItems(
//    @Element(name="item")
//    val list: List<MedicineDtoItem>? = null
//)
//
//
//@Xml
//data class MedicineDtoItem(
//    @PropertyElement(name = "ITEM_SEQ")
//    val itemSeq: String? = null,
//
//    @PropertyElement(name = "ITEM_NAME")
//    val itemName: String? = null,
//
//    @PropertyElement(name = "ENTP_NAME")
//    val entpName: String? = null,
//
//    @PropertyElement(name = "CHART")
//    val chart: String? = null,
//
//    @PropertyElement(name = "STORAGE_METHOD")
//    val storageMethod: String? = null,
//
//    @Element(name = "EE_DOC_DATA")
//    val eeDocData: String,
//
//    @Element(name = "UD_DOC_DATA")
//    val udDocData: String,
//
//    @Element(name = "NB_DOC_DATA")
//    val nbDocData: String,
//)
//
//@Xml(name = "EE_DOC_DATA")
//data class EeDocData(
//    @Element(name = "DOC")
//    val doc: Doc? = null
//)
//
//@Xml(name = "UD_DOC_DATA")
//data class UdDocData(
//    @Element(name = "DOC")
//    val doc: Doc? = null
//)
//
//@Xml(name = "NB_DOC_DATA")
//data class NbDocData(
//    @Element(name = "DOC")
//    val doc: Doc? = null
//)
//
//@Xml(name = "DOC")
//data class Doc(
//    @PropertyElement(name = "title")
//    val title: String? = null,
//
//    @PropertyElement(name = "type")
//    val type: String? = null,
//
//    @Element(name = "SECTION")
//    val section: Section? = null
//)
//
//
//@Xml(name = "SECTION")
//data class Section(
//    @Element(name = "ARTICLE")
//    val article: Article? = null
//)
//
//@Xml(name = "ARTICLE")
//data class Article(
//    @Element(name = "PARAGRAPH")
//    val paragraph: List<Paragraph>? = null
//)
//
//@Xml(name = "PARAGRAPH")
//data class Paragraph(
//    @PropertyElement(name = "tagName")
//    val tagName: String? = null,
//
//    @PropertyElement(name = "textIndent")
//    val textIndent: String? = null,
//
//    @PropertyElement(name = "marginLeft")
//    val marginLeft: String? = null,
//
//    @PropertyElement(name = "text")
//    val text: String? = null
//)






