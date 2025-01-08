package com.easternkite.remain.application

import com.easternkite.remain.enum.Keyword
import java.time.Duration

object MessageUtil {
    /**
     * 키워드별 안내 메시지 반환
     */
    fun getMessage(keyword: String, duration: Duration): String =
        when (keyword) {
            Keyword.CLOCK_OUT.value -> getClockOutMessage(duration)
            else -> getDefaultMessage(keyword, duration)
    }

    private fun getClockOutMessage(duration: Duration): String {
        val totalMinutes = duration.toMinutes()
        return when {
            totalMinutes < -60 -> ":sparkles: ${Keyword.CLOCK_OUT.value} 시간 ${-duration.toHours()}시간 ${-duration.toMinutesPart()}분 초과되었습니다. :joy:"
            totalMinutes < 0 -> ":sparkles: ${Keyword.CLOCK_OUT.value} 시간 ${-duration.toMinutesPart()}분 초과되었습니다. :joy:"
            totalMinutes == 0L -> ":sparkles: ${Keyword.CLOCK_OUT.value} 시간입니다 :tada: :tada:"
            totalMinutes < 60 -> ":sparkles: ${Keyword.CLOCK_OUT.value} 시간까지 ${duration.toMinutesPart()}분 남았습니다. :smile:"
            else -> ":sparkles: ${Keyword.CLOCK_OUT.value} 시간까지 ${duration.toHours()}시간 ${duration.toMinutesPart()}분 남았습니다. :joy:"
        }
    }

    private fun getDefaultMessage(keyword: String, duration: Duration): String {
        val totalMinutes = duration.toMinutes()
        return when {
            totalMinutes < -60 -> "${keyword} ${-duration.toHours()} 시간 ${-duration.toMinutesPart()}분 초과되었습니다. :warning:"
            totalMinutes < 0 -> "${keyword} 시간 ${-duration.toMinutesPart()}분 초과되었습니다. :warning:"
            totalMinutes == 0L -> "${keyword} 시간입니다 :heavy_check_mark:"
            totalMinutes < 60 -> "${keyword} 시간까지 ${duration.toMinutesPart()}분 남았습니다. :timer_clock:"
            else -> "${keyword} 시간까지 ${duration.toHours()}시간 ${duration.toMinutesPart()}분 남았습니다. :timer_clock:"
        }
    }
}
