package com.easternkite.remain.dooray

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val actions: List<Action> = listOf(),
    val authorLink: String = "",
    val authorName: String = "",
    val callbackId: String = "",
    val fields: List<Field> = listOf(),
    val imageUrl: String = "",
    val text: String = "",
    val thumbUrl: String = "",
    val title: String = "",
    val titleLink: String = "",
    val color: String = ""
)