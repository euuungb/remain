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
                val (time, keyword) = body.text.split(" ", limit = 2).run {
                    if (size > 1) this[0] to this[1] else this[0] to ""
                }
                val timeFormatted = time.fracFormat()

                val format = DateTimeFormatter.ofPattern("HH:mm")
                val inputTime = LocalTime.parse(timeFormatted, format)
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
            }.onFailure { error ->
                call.respond("${HttpStatusCode.BadRequest} - ${error.message}")
            }
        }
    }
}

fun String.fracFormat(): String {
    return when {
        contains('.') -> {
            val (hour, frac) = split(".").map(String::toInt)
            require(frac in 0..9) { "Invalid fraction value: $frac. Allowed range is 0-9." }
            val minute = frac * 6
            "%02d:%02d".format(hour, minute) // "HH:mm" 형식 반환
        }
        !contains(':') -> {
            "%02d:00".format(toInt())       //  정수 입력 케이스
        }
        else -> this
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
