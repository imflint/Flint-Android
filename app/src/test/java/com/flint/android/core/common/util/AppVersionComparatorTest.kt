package com.flint.android.core.common.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 강제 업데이트 판단 테스트
 *
 * 서버 계약: x.y.z 세 자리로 비교해 최소 버전보다 낮을 때만 막고, 판단할 수 없으면 통과시킨다.
 */
class AppVersionComparatorTest {

    @Test
    fun `최소 버전보다 낮으면 업데이트 대상이다`() {
        assertTrue(isUpdateRequired(currentVersion = "1.0.0", minimumVersion = "1.0.1"))
        assertTrue(isUpdateRequired(currentVersion = "1.0.9", minimumVersion = "1.1.0"))
        assertTrue(isUpdateRequired(currentVersion = "1.9.9", minimumVersion = "2.0.0"))
    }

    @Test
    fun `최소 버전과 같거나 높으면 통과한다`() {
        assertFalse(isUpdateRequired(currentVersion = "1.0.0", minimumVersion = "1.0.0"))
        assertFalse(isUpdateRequired(currentVersion = "1.2.0", minimumVersion = "1.1.9"))
        assertFalse(isUpdateRequired(currentVersion = "2.0.0", minimumVersion = "1.9.9"))
    }

    @Test
    fun `자리는 문자열이 아니라 숫자로 비교한다`() {
        assertTrue(isUpdateRequired(currentVersion = "1.9.0", minimumVersion = "1.10.0"))
        assertFalse(isUpdateRequired(currentVersion = "1.10.0", minimumVersion = "1.9.0"))
    }

    @Test
    fun `버전 뒤의 접미사는 무시한다`() {
        assertTrue(isUpdateRequired(currentVersion = "1.0.0-debug", minimumVersion = "1.0.1"))
        assertFalse(isUpdateRequired(currentVersion = "1.0.1-debug", minimumVersion = "1.0.1"))
    }

    @Test
    fun `형식이 맞지 않으면 막지 않고 통과시킨다`() {
        assertFalse(isUpdateRequired(currentVersion = "1.0.0", minimumVersion = ""))
        assertFalse(isUpdateRequired(currentVersion = "1.0.0", minimumVersion = "1.1"))
        assertFalse(isUpdateRequired(currentVersion = "1.0.0", minimumVersion = "1.x.0"))
        assertFalse(isUpdateRequired(currentVersion = "1.0", minimumVersion = "9.9.9"))
    }
}
