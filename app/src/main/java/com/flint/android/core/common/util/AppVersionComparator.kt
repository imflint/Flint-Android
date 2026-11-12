package com.flint.android.core.common.util

/**
 * 현재 앱 버전이 서버가 내려준 최소 지원 버전보다 낮은지 판단한다.
 *
 * 서버 계약상 버전은 `x.y.z` 세 자리로 비교한다. 자리마다 숫자로 비교하므로 1.10.0 > 1.9.0 이다.
 * 둘 중 하나라도 형식이 맞지 않으면 false 를 돌려 앱을 통과시킨다 (잘못된 정책 값으로 앱 전체가 막히지 않도록).
 */
fun isUpdateRequired(
    currentVersion: String,
    minimumVersion: String,
): Boolean {
    val current = currentVersion.toVersionParts() ?: return false
    val minimum = minimumVersion.toVersionParts() ?: return false

    for (i in 0 until VERSION_PART_COUNT) {
        if (current[i] != minimum[i]) return current[i] < minimum[i]
    }
    return false
}

private const val VERSION_PART_COUNT = 3

// "1.2.3" → [1, 2, 3]. "1.2.3-beta" 처럼 숫자 뒤에 붙은 접미사는 버리고, 자리가 모자라거나 숫자가 아니면 null
private fun String.toVersionParts(): List<Int>? {
    val parts = trim().substringBefore('-').split('.')
    if (parts.size != VERSION_PART_COUNT) return null
    return parts.map { it.toIntOrNull() ?: return null }
}
