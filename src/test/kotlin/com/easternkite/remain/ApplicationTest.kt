package com.easternkite.remain

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testTime() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.post("/time") {
            contentType(ContentType.Application.Json)
            setBody(
                DrRequestBody(
                    text = "퇴근 19:00",
                    command = "/time"
                )
            )
        }.apply {
            println("response = ${bodyAsText()}")
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
