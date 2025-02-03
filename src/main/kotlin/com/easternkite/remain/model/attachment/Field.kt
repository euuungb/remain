package com.easternkite.remain.model.attachment

import kotlinx.serialization.Serializable

@Serializable
data class Field(
    val short: Boolean = false,
    val title: String = "",
    val value: String = ""
)