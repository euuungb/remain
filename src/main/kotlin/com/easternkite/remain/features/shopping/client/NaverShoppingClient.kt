package com.easternkite.remain.features.shopping.client

import com.easternkite.remain.features.shopping.model.ShoppingResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class NaverShoppingClient(
    private val clientId: String,
    private val clientSecret: String
) {
    private val client = HttpClient(CIO)

    suspend fun searchProducts(
        query: String,
        sort: String = "sim",
        filter: String? = null,
    ): HttpResponse {
        val response = client.get("https://openapi.naver.com/v1/search/shop.json") {
            headers {
                append("X-Naver-Client-Id", clientId)
                append("X-Naver-Client-Secret", clientSecret)
            }
            parameter("query", query)
            parameter("display", 100)
            parameter("sort", sort)
            parameter("start", 1)
            parameter("filter", filter)
        }

        return response
    }
} 