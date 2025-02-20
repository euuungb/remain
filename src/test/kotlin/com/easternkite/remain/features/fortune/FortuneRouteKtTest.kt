package com.easternkite.remain.features.fortune

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidateBirthTest {

    @Test
    fun `생년월일이 6자 이상일 때 false`() {
        assertFalse(validateBirth("9709088"))
    }

    @Test
    fun `생년월일중, 태어난 달이 12월을 초과할 때 false`() {
        assertFalse(validateBirth("971308"))
    }

    @Test
    fun `생년월일중, 태어난 일이 31일을 초과할 때 false`() {
        assertFalse(validateBirth("970132"))
    }

    @Test
    fun `태어난 월, 일이 0일 때 false`() {
        assertFalse(validateBirth("000000"))
    }

    @Test
    fun `태어난 월, 일이 99일 때 false`() {
        assertFalse(validateBirth("999999"))
    }

    @Test
    fun `99년 12월 31일 경계값 true`() {
        assertTrue(validateBirth("991231"))
    }

    @Test
    fun `00년 01월 01일 경계값 true`() {
        assertTrue(validateBirth("000101"))
    }

    @Test
    fun `한글, 특수문자 들어가 있는 경우 false`() {
        assertFalse(validateBirth("__--얍얍"))
    }
}