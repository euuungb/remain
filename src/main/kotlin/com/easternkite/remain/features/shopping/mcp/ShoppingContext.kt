package com.easternkite.remain.features.shopping.mcp

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingContext @OptIn(ExperimentalSerializationApi::class) constructor(
    val query: String,
    @EncodeDefault val display: Int = 1,
    val sort: String = "sim",
    val filter: String? = null,
    val exclude: String? = null,
    val extra: String? = null,
)