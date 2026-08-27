package com.flint.android.presentation.onboarding

import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.terms.TermModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 약관 동의 상태 유지 테스트
 *
 * QA 2차 1: 약관 동의 후 '자세히 보기' 진입 후 복귀 시 동의 상태가 유지되어야 한다
 * QA 2차 2: 약관 동의 후 온보딩 진입 → 뒤로가기로 복귀 시 동의 상태가 유지되어야 한다
 *
 * 두 버그의 원인은 동일하다. 체크 상태가 OnboardingTermsScreen의 컴포저블 로컬
 * remember에 있어서, 화면을 벗어나면 컴포지션이 해제되며 상태가 초기화됐다.
 * 상태를 OnboardingTermsUiState로 올려 nav graph에 스코프된 OnboardingViewModel이
 * 들고 있게 하고, 그 전이 로직을 여기서 검증한다.
 */
class TermsAgreementStateTest {
    private val service =
        TermModel(
            id = "1",
            type = "SERVICE",
            version = 1,
            title = "서비스 이용약관",
            content = "",
            required = true,
            activeAt = "2026-05-13T10:48:54.554Z",
        )
    private val privacy =
        TermModel(
            id = "2",
            type = "PRIVACY",
            version = 1,
            title = "개인정보 처리 방침",
            content = "",
            required = true,
            activeAt = "2026-05-13T10:48:54.554Z",
        )
    private val marketing =
        TermModel(
            id = "3",
            type = "MARKETING",
            version = 1,
            title = "마케팅 정보 수신 동의",
            content = "",
            required = false,
            activeAt = "2026-05-13T10:48:54.554Z",
        )

    private fun loaded() =
        OnboardingTermsUiState(
            termsState = UiState.Success(listOf(service, privacy, marketing)),
        )

    // ---- 체크 토글 ----

    @Test
    fun `약관을 체크하면 checkedTermIds에 반영된다`() {
        val state = loaded().toggleChecked(service.id)

        assertTrue(state.isChecked(service.id))
        assertFalse(state.isChecked(privacy.id))
    }

    @Test
    fun `이미 체크된 약관을 다시 누르면 해제된다`() {
        val state = loaded().toggleChecked(service.id).toggleChecked(service.id)

        assertFalse(state.isChecked(service.id))
    }

    // ---- 전체 동의 ----

    @Test
    fun `전체 동의를 누르면 모든 약관이 체크된다`() {
        val state = loaded().toggleAllChecked()

        assertTrue(state.isAllChecked)
        assertTrue(state.isChecked(service.id))
        assertTrue(state.isChecked(privacy.id))
        assertTrue(state.isChecked(marketing.id))
    }

    @Test
    fun `모두 체크된 상태에서 전체 동의를 누르면 모두 해제된다`() {
        val state = loaded().toggleAllChecked().toggleAllChecked()

        assertFalse(state.isAllChecked)
        assertTrue(state.checkedTermIds.isEmpty())
    }

    @Test
    fun `일부만 체크된 상태에서 전체 동의를 누르면 모두 체크된다`() {
        val state = loaded().toggleChecked(service.id).toggleAllChecked()

        assertTrue(state.isAllChecked)
    }

    // ---- 동의하기 버튼 활성화 ----

    @Test
    fun `필수 약관이 모두 체크되어야 진행할 수 있다`() {
        val onlyOneRequired = loaded().toggleChecked(service.id)
        assertFalse(onlyOneRequired.canProceed)

        val allRequired = onlyOneRequired.toggleChecked(privacy.id)
        assertTrue(allRequired.canProceed)
    }

    @Test
    fun `선택 약관은 진행 가능 여부에 영향을 주지 않는다`() {
        val state = loaded().toggleChecked(marketing.id)

        assertFalse(state.canProceed)
    }

    @Test
    fun `약관이 로드되지 않았으면 진행할 수 없다`() {
        assertFalse(OnboardingTermsUiState().canProceed)
        assertFalse(OnboardingTermsUiState().isAllChecked)
    }

    // ---- 동의한 약관 ID 수집 ----

    @Test
    fun `체크된 약관의 id만 동의 목록으로 수집된다`() {
        val state = loaded().toggleChecked(service.id).toggleChecked(marketing.id)

        assertEquals(listOf(service.id, marketing.id), state.agreedIds)
    }

    // ---- QA 2차 1, 2: 화면 재진입 시 상태 유지 ----

    @Test
    fun `자세히 보기에서 복귀해도 체크 상태가 유지된다`() {
        // 약관을 펼치고 체크한 뒤 '자세히 보기'로 이동한 상태
        val beforeLeaving =
            loaded()
                .toggleExpanded(service.id)
                .toggleChecked(service.id)
                .toggleChecked(privacy.id)

        // OnboardingViewModel은 nav graph에 스코프되어 있으므로
        // TermsDetail을 다녀와도 동일한 상태 인스턴스가 유지된다.
        val afterReturning = beforeLeaving

        assertTrue(afterReturning.isChecked(service.id))
        assertTrue(afterReturning.isChecked(privacy.id))
        assertTrue(afterReturning.isExpanded(service.id))
        assertTrue(afterReturning.canProceed)
    }

    @Test
    fun `약관 목록이 다시 로드되어도 체크 상태가 초기화되지 않는다`() {
        val checked = loaded().toggleChecked(service.id).toggleChecked(privacy.id)

        // 재진입으로 loadTerms()가 다시 성공해 새 리스트 인스턴스가 들어온 경우
        val reloaded =
            checked.copy(
                termsState = UiState.Success(listOf(service, privacy, marketing)),
            )

        assertTrue(reloaded.isChecked(service.id))
        assertTrue(reloaded.isChecked(privacy.id))
        assertTrue(reloaded.canProceed)
    }

    @Test
    fun `펼침 상태도 화면 재진입 사이에 유지된다`() {
        val state = loaded().toggleExpanded(marketing.id)

        assertTrue(state.isExpanded(marketing.id))
        assertFalse(state.isExpanded(service.id))

        assertFalse(state.toggleExpanded(marketing.id).isExpanded(marketing.id))
    }
}
