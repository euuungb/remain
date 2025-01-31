package com.easternkite.remain.dooray

import kotlinx.serialization.Serializable

@Serializable
data class Action(
    val dataSource: String = "",
    val name: String = "",
    val text: String = "",
    val type: String = "",
    val value: String = ""
)