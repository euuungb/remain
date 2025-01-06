package com.easternkite.remain

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class DoorayTagTest {
    @Test
    fun `tenentId가 비어있으면 빈 문자열을 반환한다`() {
        val expected = ""
        val actual = DoorayTag.create("", userId = "12345")
        assertEquals(expected, actual)
    }

    @Test
    fun `userId가 비어있으면 빈 문자열을 반환한다`() {
        val expected = ""
        val actual = DoorayTag.create("12345", userId = "")
        assertEquals(expected, actual)
    }

    @Test
    fun `userId 혹은 tenentId에 숫자 이외의 문자가 있다면 빈 문자열을 반환한다`() {
        val expected = ""
        val actual = DoorayTag.create("12345", userId = "abcde")
        assertEquals(expected, actual)
        val actual2 = DoorayTag.create("abcde", userId = "12345")
        assertEquals(expected, actual2)
    }

    @Test
    fun `둘다 비어있지 않으면, Dooray 태그를 생성한다`() {
        val expected = "(dooray://12345/members/12345 \"member\")"
        val actual = DoorayTag.create("12345", userId = "12345")
        assertEquals(expected, actual)
    }

    @Test
    fun `별칭이 있는경우, 그에 맞는 태그를 생성한다`() {
        val expected = "[별칭](dooray://12345/members/12345 \"member\")"
        val actual = DoorayTag.create("12345", userId = "12345", alias = "별칭")
        assertEquals(expected, actual)
    }

    @Test
    fun `tenantId, userId가 유효하지 않다면, 별칭이 있더라도 공백을 반환한다`() {
        val expected = ""
        val actual = DoorayTag.create("abcde", userId = "12345", alias = "별칭")
        assertEquals(expected, actual)
        val actual2 = DoorayTag.create("12345", userId = "abcde", alias = "별칭")
        assertEquals(expected, actual2)
        val actual3 = DoorayTag.create("", userId = "", alias = "별칭")
        assertEquals(expected, actual3)
    }
}