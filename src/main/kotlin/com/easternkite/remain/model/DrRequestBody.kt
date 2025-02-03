package com.easternkite.remain.model

import kotlinx.serialization.Serializable

@Serializable
data class DrRequestBody(
    val channelId: String = "",
    val channelName: String = "",
    val tenantId: String = "",
    val tenantDomain: String = "",
    val command: String = "",
    val responseUrl: String = "",
    val text: String = "",
    val appToken: String = "",
    val cmdToken: String = "",
    val triggerId: String = "",
    val userId: String = "",
    val userEmail: String = ""
)
