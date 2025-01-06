package com.easternkite.remain

/**
 * Dooray 메신저에 사람 이름 태그 추가하기 위함
 * @author easternkite
 */
object DoorayTag {
    /**
     * id 밸리데이션
     * @author easternkite
     * @param tenantId 요청 발신자의 tenantId
     * @param userId 요청 발신자의 사용자 id
     * @return 유효성 검사 결과
     */
    fun validate(tenantId: String, userId: String): Boolean {
        if (!validateId(tenantId) || !validateId(userId)) return false
        return tenantId.isNotEmpty() && userId.isNotEmpty()
    }

    /**
     * id가 숫자로만 이루어져 있는지 확인
     * @author easternkite
     * @param id 확인할 id
     * @return 유효성 검사 결과
     */
    private fun validateId(id: String): Boolean {
        id.forEach {
            if (!it.isDigit()) return false
        }
        return true
    }
}

/**
 * Dooray 태그 생성
 * @author easternkite
 * @param tenantId 요청 발신자의 tenantId
 * @param userId 요청 발신자의 사용자 id
 */
fun DoorayTag.create(tenantId: String, userId: String): String {
    val isValid = validate(tenantId, userId)
    if (!isValid) return ""
    return "(dooray://${tenantId}/members/$userId \"member\")"
}

/**
 * Dooray 메신저에서 별칭으로 태그
 * @author easternkite
 * @param tenantId 요청 발신자의 tenantId
 * @param userId 요청 발신자의 사용자 id
 * @param alias 별칭
 */
fun DoorayTag.create(tenantId: String, userId: String, alias: String): String {
    val tag = create(tenantId, userId)
    if (tag.isEmpty()) return ""
    return "[$alias]$tag"
}