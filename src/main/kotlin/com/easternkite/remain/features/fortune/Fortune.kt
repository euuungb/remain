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
    val total: String,
    /**
     * 금전운
     */
    val money: String,
    /**
     * 연애운
     */
    val love: String,
    /**
     * 건강운
     */
    val health: String,
)