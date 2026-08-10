package com.flint.presentation.onboarding

import com.flint.presentation.setting.editprofile.EditProfileUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 닉네임 유효성 검사 규칙 테스트
 *
 * QA TC 1-34: 한글/영문/숫자 혼합 2~8자는 정상 입력 가능해야 한다
 * QA TC 1-40: 특수문자, 기호, 이모지, 한글/영문 외 다른 언어는 거부되어야 한다
 *
 * 온보딩(OnboardingProfileUiState)과 프로필 수정(EditProfileUiState)에
 * 동일한 규칙이 각각 구현돼 있어 양쪽 모두 검증한다.
 */
class NicknameValidationTest {

    private fun bothAccept(nickname: String): Boolean {
        val onboarding = OnboardingProfileUiState.isValidFormat(nickname)
        val editProfile = EditProfileUiState.isValidFormat(nickname)
        assertEquals(
            "온보딩과 프로필 수정의 판정이 달라선 안 된다: \"$nickname\"",
            onboarding,
            editProfile,
        )
        return onboarding
    }

    // ---- TC 1-34: 숫자 허용 ----

    @Test
    fun `숫자만으로 이루어진 닉네임을 허용한다`() {
        assertTrue(bothAccept("1234"))
    }

    @Test
    fun `한글과 숫자를 섞은 닉네임을 허용한다`() {
        assertTrue(bothAccept("플린트2"))
    }

    @Test
    fun `영문과 숫자를 섞은 닉네임을 허용한다`() {
        assertTrue(bothAccept("flint2"))
    }

    @Test
    fun `한글 영문 숫자를 모두 섞은 닉네임을 허용한다`() {
        // MAX_LENGTH(8) 이내로 둬야 실제 입력 흐름에서 도달 가능한 값이 된다
        assertTrue(bothAccept("플린트fl7"))
    }

    // ---- 기존 허용 범위 회귀 ----

    @Test
    fun `한글만으로 이루어진 닉네임을 허용한다`() {
        assertTrue(bothAccept("플린트"))
    }

    @Test
    fun `영문만으로 이루어진 닉네임을 허용한다`() {
        assertTrue(bothAccept("Flint"))
    }

    @Test
    fun `빈 문자열은 형식 오류로 보지 않는다`() {
        // 입력 전 상태에서 에러 메시지가 뜨면 안 되므로 형식 검사는 통과시킨다
        assertTrue(bothAccept(""))
    }

    // ---- TC 1-40: 거부 대상 ----

    @Test
    fun `특수문자가 포함되면 거부한다`() {
        assertFalse(bothAccept("플린트!"))
        assertFalse(bothAccept("flint_2"))
        assertFalse(bothAccept("flint 2"))
    }

    @Test
    fun `이모지가 포함되면 거부한다`() {
        assertFalse(bothAccept("플린트🔥"))
    }

    @Test
    fun `한글 영문 외 다른 언어는 거부한다`() {
        assertFalse(bothAccept("플린트あ"))
        assertFalse(bothAccept("플린트中"))
    }

    // ---- 자음/모음 단독 입력 ----

    @Test
    fun `자음이나 모음 단독 입력을 감지한다`() {
        // 온보딩은 완성 음절만 허용하는 형식 검사로 자모 단독 입력을 걸러낸다.
        assertFalse(OnboardingProfileUiState.isValidFormat("ㅋㅋㅋ"))
        assertTrue(EditProfileUiState(nickname = "ㅋㅋㅋ").hasStandaloneKorean)

        assertTrue(OnboardingProfileUiState.isValidFormat("플린트2"))
        assertFalse(EditProfileUiState(nickname = "플린트2").hasStandaloneKorean)
    }
}
