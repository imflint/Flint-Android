package com.flint.android.presentation.onboarding

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.terms.TermModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 약관 동의 상태가 화면 이동 후에도 유지되는지 실제 NavHost로 검증한다.
 *
 * QA 2차 1: 약관 동의 후 '자세히 보기' 진입 → 복귀 시 동의 상태 유지
 * QA 2차 2: 약관 동의 후 온보딩 진입 → 뒤로가기 복귀 시 동의 상태 유지
 *
 * 프로덕션과 동일하게 상태를 화면 바깥(그래프 스코프)에 두고 실제로 이동/복귀시킨다.
 * 수정 전에는 체크 상태가 OnboardingTermsScreen 내부 remember에 있어,
 * 복귀 시 컴포지션이 새로 만들어지며 초기화됐다.
 */
@RunWith(AndroidJUnit4::class)
class OnboardingTermsNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

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

    private lateinit var navController: NavHostController

    private fun setContent() {
        composeRule.setContent {
            navController = rememberNavController()

            // 프로덕션에서 OnboardingViewModel이 nav graph에 스코프되어 하는 역할.
            // NavHost 바깥에 두어 목적지 이동에도 살아남게 한다.
            var termsState by remember {
                mutableStateOf(
                    OnboardingTermsUiState(
                        termsState = UiState.Success(listOf(service, privacy, marketing)),
                    ),
                )
            }

            FlintTheme {
                NavHost(navController = navController, startDestination = "terms") {
                    composable("terms") {
                        OnboardingTermsScreen(
                            termsUiState = termsState,
                            onBackClick = {},
                            onCheckClick = { termsState = termsState.toggleChecked(it) },
                            onAllCheckClick = { termsState = termsState.toggleAllChecked() },
                            onExpandClick = { termsState = termsState.toggleExpanded(it) },
                            onAgreeClick = { navController.navigate("next") },
                            onDetailClick = { navController.navigate("detail") },
                        )
                    }
                    composable("detail") { Text("약관 상세 화면") }
                    composable("next") { Text("온보딩 다음 단계") }
                }
            }
        }
    }

    private fun goBack() {
        composeRule.runOnUiThread { navController.popBackStack() }
        composeRule.waitForIdle()
    }

    private fun checkRequiredTerms() {
        composeRule.onNodeWithContentDescription("${service.title} 동의").performClick()
        composeRule.onNodeWithContentDescription("${privacy.title} 동의").performClick()
    }

    // ---- QA 2차 1 ----

    @Test
    fun `자세히 보기에서 복귀해도 동의 상태가 유지된다`() {
        setContent()

        checkRequiredTerms()
        composeRule.onNodeWithText("동의하기").assertIsEnabled()

        // 약관을 펼쳐 '자세히 보기'로 이동
        composeRule.onNodeWithContentDescription("${service.title} 펼치기").performClick()
        composeRule.onNodeWithText("자세히 보기").performClick()
        composeRule.onNodeWithText("약관 상세 화면").assertExists()

        goBack()

        // 체크와 펼침 상태가 그대로여야 한다
        composeRule.onNodeWithText("동의하기").assertIsEnabled()
        composeRule.onNodeWithText("자세히 보기").assertExists()
    }

    // ---- QA 2차 2 ----

    @Test
    fun `온보딩 다음 단계에서 뒤로가기로 복귀해도 동의 상태가 유지된다`() {
        setContent()

        checkRequiredTerms()
        composeRule.onNodeWithText("동의하기").assertIsEnabled()

        composeRule.onNodeWithText("동의하기").performClick()
        composeRule.onNodeWithText("온보딩 다음 단계").assertExists()

        goBack()

        composeRule.onNodeWithText("동의하기").assertIsEnabled()
    }

    // ---- 전체 동의 ----

    @Test
    fun `전체 동의 후 이동했다 복귀해도 전체 체크가 유지된다`() {
        setContent()

        composeRule.onNodeWithText("전체 동의").performClick()
        composeRule.onNodeWithText("동의하기").assertIsEnabled()

        composeRule.onNodeWithText("동의하기").performClick()
        composeRule.onNodeWithText("온보딩 다음 단계").assertExists()

        goBack()

        composeRule.onNodeWithText("동의하기").assertIsEnabled()
        // 선택 약관까지 체크된 상태가 유지되는지 확인 — 다시 누르면 전체 해제되어야 한다
        composeRule.onNodeWithText("전체 동의").performClick()
        composeRule.onNodeWithText("동의하기").assertIsNotEnabled()
    }

    // ---- 대조군 ----

    @Test
    fun `필수 약관을 체크하지 않으면 동의하기가 비활성화된다`() {
        setContent()

        composeRule.onNodeWithText("동의하기").assertIsNotEnabled()

        composeRule.onNodeWithContentDescription("${marketing.title} 동의").performClick()
        composeRule.onNodeWithText("동의하기").assertIsNotEnabled()
    }
}
