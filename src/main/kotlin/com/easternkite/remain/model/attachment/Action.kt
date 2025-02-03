package com.easternkite.remain.model.attachment

import kotlinx.serialization.Serializable

@Serializable
data class Action(
    val dataSource: String = "",
    val name: String = "",
    val text: String = "",
    val type: String = "",
    val value: String = ""
)