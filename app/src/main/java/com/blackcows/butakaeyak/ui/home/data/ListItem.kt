package com.blackcows.butakaeyak.ui.home.data

sealed class ListItem {
    data class PillResultItem(val name : String) : ListItem()
    data class FeedItem(val name : String) : ListItem()
}