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
                val errorResponse = DrResponse(
                    text = "${HttpStatusCode.BadRequest} - ${error.message}",
                    responseType = "ephemeral" // 작성자한테만 표시
                )

                call.respondText(
                    text = Json.encodeToString(errorResponse),
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
}


/**
 * 입력된 시간을 "HH:mm" 형식으로 변환합니다.
 * - 소수 입력: "20.5" → "20:30" (소수점은 1자리까지만 허용)
 * - 정수 입력: "20" → "20:00"
 * - 기존 형식: "20:30" → 그대로 반환
 *
 * @return 변환된 "HH:mm" 형식의 시간 문자열
 * @throws IllegalArgumentException 유효하지 않은 소수점 입력인 경우
 */
fun String.fracFormat(): String {
    return when {
        contains('.') -> {
            val (hour, frac) = split(".").map(String::toInt)
            require(frac in 0..9) { "Invalid fraction value: $frac. Allowed range is 0-9." }
            val minute = frac * 6
            "%02d:%02d".format(hour, minute)
        }
        !contains(':') -> {
            "%02d:00".format(toInt())
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
