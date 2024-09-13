package com.blackcows.butakaeyak.data.models

data class Friend(
    val proposer: String,
    val receiver: String,
    val isConnected: Boolean
)