package com.easternkite.remain.features.fortune

import kotlinx.serialization.Serializable

@Serializable
data class Fortune(
    /**
     * 띠 Ex) 쥐띠, 소띠
     */
    val zodiac: String,
    /**
     * 총운
     */
    val total: Section,
    /**
     * 금전운
     */
    val money: Section,
    /**
     * 연애운
     */
    val love: Section,
    /**
     * 건강운
     */
    val health: Section,
)

@Serializable
data class Section(
    /**
     * 운세 내용
     */
    val text: String,
    /**
     * 운세에 따른 색상
     */
    val color: String
)