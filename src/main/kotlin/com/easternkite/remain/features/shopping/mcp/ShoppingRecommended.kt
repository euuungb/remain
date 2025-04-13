package com.easternkite.remain.features.shopping.mcp

import com.easternkite.remain.model.attachment.Attachment
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingRecommended(
    val title: String,
    val price: Int,
    val url: String,
    val imageUrl: String,
    val reason: String,
)

fun ShoppingRecommended.toAttachment(): Attachment {
    return Attachment(
        title = title,
        titleLink = url,
        text = reason,
        authorName = price.formatWon(),
        imageUrl = imageUrl
    )
}

fun Int.formatWon(): String {
    return "%,d원".format(this)
}