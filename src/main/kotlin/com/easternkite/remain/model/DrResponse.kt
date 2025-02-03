package com.easternkite.remain.model

import kotlinx.serialization.Serializable

@Serializable
data class DrResponse(
    val text: String,
    val responseType: String
)