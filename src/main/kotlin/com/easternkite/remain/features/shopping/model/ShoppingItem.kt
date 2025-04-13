package com.easternkite.remain.features.shopping.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItem(
    val title: String,
    val link: String,
    val image: String,
    val lprice: String,
    val hprice: String,
    val mallName: String,
    val productId: String,
    val productType: String,
    val brand: String,
    val maker: String,
    val category1: String,
    val category2: String,
    val category3: String,
    val category4: String
)

@Serializable
data class ShoppingResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ShoppingItem>
) 