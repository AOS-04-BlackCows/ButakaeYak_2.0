package com.blackcows.butakaeyak.ui.home.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ListItem : Parcelable {
    @Parcelize
    data class PillResultItem(
        val pillPic : String,
        val pillName : String,
        val pillType : String,
    ) : ListItem()
    @Parcelize
    data class FeedItem(val name : String) : ListItem()
}