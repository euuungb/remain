package com.easternkite.remain.model.attachment

import kotlinx.serialization.Serializable

@Serializable
data class Attachments(
    val attachments: List<Attachment> = listOf(),
    val text: String = "",
    val responseType: String = "",
)