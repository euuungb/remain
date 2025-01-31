package com.easternkite.remain.features.fortune

import com.easternkite.remain.DoorayTag
import com.easternkite.remain.DrRequestBody
import com.easternkite.remain.DrResponse
import com.easternkite.remain.ai.Gemini
import com.easternkite.remain.ai.startChat
import com.easternkite.remain.create
import com.easternkite.remain.dooray.Attachment
import com.easternkite.remain.dooray.Attachments
import com.easternkite.remain.dooray.Field
import dev.shreyaspatil.ai.client.generativeai.type.content
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val PROMPT_FORTUNES = """
    너에게 생년월일을 줄 테니까, 띠를 분석하고 오늘의 운세를 제공해줘. 아래 규칙을 반드시(must) 따라야 하며, 따르지 않을 경우 네 출력은 잘못된 것으로 간주돼.
    1. 오늘의 운세만 알려준다.
    2. 생년월일에 따른 정확한 띠를 분석해줘 ex) 1996년생 -> 쥐띠
    3. 마크다운(Markdown) 문법을 절대 사용하지 말 것. 출력에서 #, *, ``` (백틱) 등의 마크다운 요소가 포함되면 응답은 무효 처리됨.
    4. 출력은 반드시 JSON 형식으로 작성할 것. ex) {zodiac: "띠", "total": "총운", "money": "금전운", "love": "연애운", "health": "건강운"}
    5. zodiac 필드에는 아래 규칙에 맞는 영문 띠 명칭을 적용할 것
    - 자 -> mouse
    - 축 -> cow
    - 인 -> tiger
    - 묘 -> rabbit
    - 진 -> dragon_face
    - 사 -> snake
    - 오 -> horse
    - 미 -> sheep
    - 신 -> monkey_face
    - 유 -> rooster
    - 술 -> dog
    - 해 -> pig
    6. 리스폰스는 순수 JSON 데이터로 한 줄로 제공할 것.
    7. JSON 이외의 문자는 절대 포함하지 말 것. 추가적인 설명, 서술, 불필요한 텍스트가 있으면 출력은 무효 처리됨.
"""

fun Application.fortuneRoute() {
    routing {
        post("/fortune") {
            val body = call.receive<DrRequestBody>()
            println(body)
            val chat = Gemini.startChat(
                history = listOf(
                    content {
                        text(PROMPT_FORTUNES)
                    }
                )
            )
            val birth = body
                .text
                .takeIf { it.length == 6 }
                ?.takeIf { it.matches("[0-9]+".toRegex()) }
                ?: run {
                    val errorResponse = DrResponse(
                        text = "${HttpStatusCode.BadRequest} - 생년월일은 숫자로 이루어진 6자리여야 합니다.",
                        responseType = "ephemeral" // 작성자한테만 표시
                    )

                    call.respondText(
                        text = Json.encodeToString(errorResponse),
                        contentType = ContentType.Application.Json
                    )
                    return@post
                }
            val userTag = DoorayTag.create(
                tenantId = body.tenantId,
                userId = body.userId
            )
            val response = chat.sendMessage("생년월일 : ${birth}")
            val fortune = Json.decodeFromString<Fortune>(response.text ?: "")
            val attachmentList = listOf(
                Field(
                    short = true,
                    title = "총운",
                    value = fortune.total
                ),
                Field(
                    short = true,
                    title = "연애운",
                    value = fortune.love
                ),
                Field(
                    short = true,
                    title = "금전운",
                    value = fortune.money
                ),
                Field(
                    short = true,
                    title = "건강운",
                    value = fortune.health
                ),
            ).map { Attachment(fields = listOf(it)) }

            val attachments = Attachments(
                text = "$userTag 님의 오늘의 운세입니다 :${fortune.zodiac}:",
                attachments = attachmentList
            )
            call.respondText(
                Json.encodeToString(attachments),
                contentType = ContentType.Application.Json
            )
        }
    }
}

