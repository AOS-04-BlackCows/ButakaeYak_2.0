package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose

data class Friend(
    @Expose(serialize = false)
    val id: String,
    val proposer: String,
    val receiver: String,
    val isConnected: Boolean
)