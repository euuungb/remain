package com.easternkite.remain.features.fortune

import com.easternkite.remain.DoorayTag
import com.easternkite.remain.ai.Gemini
import com.easternkite.remain.ai.startChat
import com.easternkite.remain.create
import com.easternkite.remain.model.DrRequestBody
import com.easternkite.remain.model.DrResponse
import com.easternkite.remain.model.attachment.Attachment
import com.easternkite.remain.model.attachment.Attachments
import com.easternkite.remain.model.attachment.Field
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val PROMPT_FORTUNES = """
    너에게 생년월일과 기준 날짜를 줄 테니까, 띠를 분석하고 오늘의 운세를 제공해줘. 아래 규칙을 반드시(must) 따라야 하며, 따르지 않을 경우 네 출력은 잘못된 것으로 간주돼.
    1. 기준 날짜 기준의 운세를 알려줘. 생년월일은 알아서 잘 판단해줘. 031205 -> 2003년 12월 5일, 440118 -> 1944년 1월 18일
    2. 생년월일에 따른 정확한 띠를 분석해서 활용하고, zodiac 필드에 넣어줄 것 ex) 1996년생 -> 쥐띠
    3. 마크다운(Markdown) 문법을 절대 사용하지 말 것. 출력에서 #, *, ``` (백틱) 등의 마크다운 요소가 포함되면 응답은 무효 처리됨.
    4. 각각의 항목별로, 너가 원하는 색상 hex 코드를 추가할 것. 넓은 범위의 색상으로 고려할 것. ex) #ffffff, 해당 색상 코드는 만들어야할 Json 포맷에서 반드시 color 필드에만 채워넣을 것. 운세 내용에는 절대 넣지 말 것.
    5. 출력은 반드시 JSON 형식으로 작성할 것. ex) {zodiac: "띠", "total": { "text" : "총운", "color" : "#ffffff" }, "money": { "text" : "금전운", "color" : "#ffffff" }, "love": { "text" : "연애운", "color" : "#ffffff" }, "health": { "text" : "건강운", "color" : "#ffffff" }}
    6. zodiac 필드에는 아래 규칙에 맞는 영문 띠 명칭을 적용할 것
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
    7. 리스폰스는 순수 JSON 데이터로 한 줄로 제공할 것 반드시 5번에서 예시로 들어준 포맷에 맞출 것.
    8. 너의 MZ력을 충분히 발휘해서 킹받는 말투로 길게 말할 것. 초성표현 적극 사용 권장. 구체적이고 귀엽게 표현할 것. 단, 띠(zodiac)를 사용해서 설명에 절대 언급하지 말 것.
    9. JSON 이외의 문자는 절대 포함하지 말 것. 추가적인 설명, 서술, 불필요한 텍스트가 있으면 출력은 무효 처리됨.
"""
fun Application.fortuneRoute() {
    routing {
        post("/fortune") {
            val body = call.receive<DrRequestBody>()
            println(body)
            val format = DateTimeFormatter.ofPattern("yyyy년 M월 d일")
            val currentTimeFormatted = LocalDateTime.now().format(format)
            val chat = Gemini.startChat(
                history = listOf(
                    content {
                        text(PROMPT_FORTUNES)
                    }
                )
            )
            val birth = body.text
            if (!validateBirth(birth)) {
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

            val response = chat.sendMessage("생년월일 : ${birth}, 기준 날짜 : $currentTimeFormatted")
            val fortuneJson = response.text?.let { text ->
                println(text)
                val isMarkedDown = text.contains("```")
                text.takeUnless { isMarkedDown }
                    ?: text.lines().drop(1).dropLast(1).joinToString()
            }
            val fortune = Json.decodeFromString<Fortune>(fortuneJson ?: "")
            val attachmentList = listOf(
                FortuneField(
                    title = "총운",
                    section = fortune.total
                ),
                FortuneField(
                    title = "연애운",
                    section = fortune.love
                ),
                FortuneField(
                    title = "금전운",
                    section = fortune.money
                ),
                FortuneField(
                    title = "건강운",
                    section = fortune.health
                ),
            ).map {
                Attachment(
                    fields = listOf(Field(title = it.title, value = it.section.text)),
                    color = it.section.color
                )
            }

            val attachments = Attachments(
                text = "$currentTimeFormatted,\n$userTag 님의 오늘의 운세입니다 :${fortune.zodiac}:",
                attachments = attachmentList,
                responseType = "inChannel"
            )
            call.respondText(
                Json.encodeToString(attachments),
                contentType = ContentType.Application.Json
            )
        }
    }
}

fun validateBirth(birth: String): Boolean {
    if (birth.length != 6) return false

    val (_, month, day) = runCatching {
        birth.chunked(2) { it.toString().toInt() }
    }.getOrNull() ?: return false

    val isPossibleMonth = month in 1..12
    val isPossibleDay = day in 1 .. 31

    return isPossibleMonth && isPossibleDay
}


data class FortuneField(
    val title: String,
    val section: Section,
)

