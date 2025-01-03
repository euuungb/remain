package com.easternkite.remain.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json

/**
 * 외부 API 요청을 위한 Client.
 * @author easternkite
 */
class RemainClient(
    engine: HttpClientEngine = CIO.create(),
    defaultRequestBlock: DefaultRequest.DefaultRequestBuilder.() -> Unit = {}
) {
    /**
     * Http Request를 하기 위한 client 인스턴스.
     * @author easternkite
     */
    val client = HttpClient(engine) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation, Configuration::json)
        install(DefaultRequest)

        defaultRequest(defaultRequestBlock)
    }
}

/**
 * GET 요청
 * @author easternkite
 * @param urlString 요청할 url 주소
 * @param block 요청시 추가적인 셋팅을 위한 builder. (Request HEADER 등)
 */
suspend inline fun <reified T> RemainClient.get(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching { client.get(urlString, block).body() }

/**
 * POST 요청
 * @author easternkite
 * @param urlString 요청할 url 주소
 * @param block 요청시 추가적인 셋팅을 위한 builder. (Request HEADER, BODY 등)
 */
suspend inline fun <reified T> RemainClient.post(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching { client.post(urlString, block).body() }

/**
 * PUT 요청
 * @author easternkite
 * @param urlString 요청할 url 주소
 * @param block 요청시 추가적인 셋팅을 위한 builder. (Request HEADER 등)
 */
suspend inline fun <reified T> RemainClient.put(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching { client.put(urlString, block).body() }

/**
 * PATCH 요청
 * @author easternkite
 * @param urlString 요청할 url 주소
 * @param block 요청시 추가적인 셋팅을 위한 builder. (Request HEADER 등)
 */
suspend inline fun <reified T> RemainClient.patch(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching { client.patch(urlString, block).body() }

/**
 * DELETE 요청
 * @author easternkite
 * @param urlString 요청할 url 주소
 * @param block 요청시 추가적인 셋팅을 위한 builder. (Request HEADER 등)
 */
suspend inline fun <reified T> RemainClient.delete(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> = runCatching { client.delete(urlString, block).body() }
