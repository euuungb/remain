package com.easternkite.remain

import com.easternkite.remain.application.MessageUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Application.configureRouting() {
    routing {
        post("/time") {
            runCatching {
                val body = call.receive<DrRequestBody>()
                val text = body.text.replace("\"", "").split(" ")
                val keyword = text[0];

                val format = DateTimeFormatter.ofPattern("HH:mm")
                val inputTime = LocalTime.parse(text[1], format)
                val currentTime = LocalTime.now()
                val duration = Duration.between(currentTime, inputTime)
                val message = MessageUtil.getMessage(keyword, duration)

                val userTag = DoorayTag.create(
                    tenantId = body.tenantId,
                    userId = body.userId
                )

                DrResponse(
                    text = "$userTag $message".trimIndent(),
                    responseType = "inChannel"
                )
            }.onSuccess {
                call.respondText(
                    text = Json.encodeToString(it),
                    contentType = ContentType.Application.Json
                )
            }.onFailure {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

@Serializable
data class DrResponse(
    val text: String,
    val responseType: String
)

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
