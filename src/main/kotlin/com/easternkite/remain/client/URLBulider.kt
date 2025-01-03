package com.easternkite.remain.client

import io.ktor.http.URLBuilder
import io.ktor.http.path

/**
 * URL path를 동적으로 변경합니다.
 * @author easternkite
 * @param key 변경하고자하는 path의 키 값. 요청시, curly-brace로 감싸야함
 * * ex. easternkite.github.io/{USER_ID}
 * @param value 바꿔야할 실제 값
 */
fun URLBuilder.dynamicPath(
    key: String,
    value: String
) {
    val reflectedSegments = pathSegments
        .map { path ->
            val containsKey = path == "{$key}"
            if (containsKey) value else path
        }.toTypedArray()

    path(*reflectedSegments)
}
