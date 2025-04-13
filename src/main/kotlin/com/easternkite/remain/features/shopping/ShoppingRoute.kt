package com.easternkite.remain.features.shopping

import com.easternkite.remain.DoorayTag
import com.easternkite.remain.create
import com.easternkite.remain.features.shopping.client.NaverShoppingClient
import com.easternkite.remain.features.shopping.mcp.ShoppingMCP
import com.easternkite.remain.features.shopping.mcp.ShoppingRecommended
import com.easternkite.remain.features.shopping.mcp.toAttachment
import com.easternkite.remain.model.DrRequestBody
import com.easternkite.remain.model.DrResponse
import com.easternkite.remain.model.attachment.Attachments
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureShoppingRouting() {
    routing {
        // 테스트용 GET 엔드포인트
//        get("/shopping/test") {
//            runCatching {
//                val context = mcp.sendUserQuery("100만원대 안쪽으로 살 수 있는 가성비 스마트폰 10개만 추천좀.. KT, SK와 같은 통신사 상품들은 제외해줘  무조건 !")
//                val recommends = mcp.createRecommendsByContext(context)
//
//                call.respondText(
//                    text = recommends.toString(),
//                    contentType = ContentType.Text.Plain
//                )
//            }.onFailure { error ->
//                call.respondText(
//                    text = "에러 발생: ${error.message}",
//                    contentType = ContentType.Text.Plain,
//                    status = HttpStatusCode.BadRequest
//                )
//            }
//        }

        post("/shopping") {
            runCatching {
                val mcp = ShoppingMCP()
                val body = call.receive<DrRequestBody>()
                val query = body.text.trim()
                if (query.isBlank()) {
                    throw IllegalArgumentException("검색어를 입력해주세요.")
                }
                val context = mcp.sendUserQuery(query)
                val userTag = DoorayTag.create(
                    tenantId = body.tenantId,
                    userId = body.userId
                )
                val recommends = mcp
                    .createRecommendsByContext(context)
                    .map(ShoppingRecommended::toAttachment)

                Attachments(
                    text = "${userTag}님을 위한 `${context.query}` !",
                    attachments = recommends,
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
                    responseType = "ephemeral"
                )

                call.respondText(
                    text = Json.encodeToString(errorResponse),
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
} 