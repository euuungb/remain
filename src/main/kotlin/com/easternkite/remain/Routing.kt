package com.easternkite.remain

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
                val target = body.text.replace("\"", "")

                val format = DateTimeFormatter.ofPattern("HH:mm")
                val inputTime = LocalTime.parse(target, format)
                val currentTime = LocalTime.now()
                val duration = Duration.between(currentTime, inputTime)

                val hours = duration.toHours()
                val minutes = duration.toMinutesPart()

                val message = if (duration.toMinutes() < -60) {
                    ":sparkles: 퇴근시간 ${-hours}시간 ${-minutes}분 초과되었습니다. :joy:"
                } else if (duration.toMinutes() < 0) {
                    ":sparkles: 퇴근시간 ${-minutes}분 초과되었습니다. :joy:"
                } else if (duration.toMinutes() == 0L) {
                    ":sparkles: 퇴근시간 입니다 :tada: :tada:"
                } else if (duration.toMinutes() < 60) {
                    ":sparkles: 퇴근까지 ${minutes}분 남았습니다. :smile:"
                } else {
                    ":sparkles: 퇴근까지 ${hours}시간 ${minutes}분 남았습니다. :joy"
                }

                DrResponse(
                    text = message,
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
    val channelId: String,
    val channelName: String,
    val tenantId: String,
    val tenantDomain: String,
    val command: String,
    val responseUrl: String,
    val text: String,
    val appToken: String,
    val cmdToken: String,
    val triggerId: String,
    val userId: String,
    val userEmail: String
)
