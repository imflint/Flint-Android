package com.flint.android.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.R
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.TrackScreenView
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.core.common.util.TermGuides
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintLargeButton
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.terms.TermModel

@Composable
fun OnboardingTermsRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOnboardingContent: () -> Unit,
    navigateToTermsDetail: (termId: String) -> Unit,
    viewModel: OnboardingViewModel,
) {
    val termsUiState by viewModel.termsUiState.collectAsStateWithLifecycle()

    TrackScreenView(FlintEvent.ViewTos)

    LaunchedEffect(Unit) {
        viewModel.loadTerms()
    }

    OnboardingTermsScreen(
        termsUiState = termsUiState,
        onBackClick = navigateUp,
        onCheckClick = viewModel::toggleTermChecked,
        onAllCheckClick = viewModel::toggleAllTermsChecked,
        onExpandClick = viewModel::toggleTermExpanded,
        onAgreeClick = {
            viewModel.agreeToTerms()
            navigateToOnboardingContent()
        },
        onDetailClick = navigateToTermsDetail,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingTermsScreen(
    termsUiState: OnboardingTermsUiState,
    onBackClick: () -> Unit,
    onCheckClick: (termId: String) -> Unit,
    onAllCheckClick: () -> Unit,
    onExpandClick: (termId: String) -> Unit,
    onAgreeClick: () -> Unit,
    onDetailClick: (termId: String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val terms = termsUiState.terms
    val requiredAllChecked = termsUiState.canProceed
    val allChecked = termsUiState.isAllChecked

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(onClick = onBackClick)

        Column(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "약관에 동의해주세요",
                    style = FlintTheme.typography.display2M28,
                    color = FlintTheme.colors.white,
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .flintCardClickable(onClick = onAllCheckClick)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (allChecked) R.drawable.ic_check_fill else R.drawable.ic_check_empty
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "전체 동의",
                        style = FlintTheme.typography.body1R16,
                        color = FlintTheme.colors.white,
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            HorizontalDivider(thickness = 4.dp, color = FlintTheme.colors.gray600)

            when (termsUiState.termsState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = FlintTheme.colors.primary200)
                    }
                }

                is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "약관을 불러오지 못했습니다.",
                            style = FlintTheme.typography.body1R16,
                            color = FlintTheme.colors.gray400,
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                    ) {
                        Spacer(Modifier.height(16.dp))

                        terms.forEach { term ->
                            TermRow(
                                term = term,
                                isChecked = termsUiState.isChecked(term.id),
                                isExpanded = termsUiState.isExpanded(term.id),
                                onCheckClick = { onCheckClick(term.id) },
                                onExpandClick = { onExpandClick(term.id) },
                                onDetailClick = { onDetailClick(term.id) },
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }

        FlintLargeButton(
            text = "동의하기",
            state = if (requiredAllChecked) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = onAgreeClick,
            enabled = requiredAllChecked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        )
    }
}

@Composable
private fun TermRow(
    term: TermModel,
    isChecked: Boolean,
    isExpanded: Boolean,
    onCheckClick: () -> Unit,
    onExpandClick: () -> Unit,
    onDetailClick: () -> Unit,
) {
    val requiredPrefix = if (term.required) "(필수) " else "(선택) "

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    if (isChecked) R.drawable.ic_check_fill else R.drawable.ic_check_empty
                ),
                contentDescription = "${term.title} 동의",
                tint = Color.Unspecified,
                modifier = Modifier.flintIconClickable(onClick = onCheckClick),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = requiredPrefix + term.title,
                style = FlintTheme.typography.body1R16,
                color = FlintTheme.colors.white,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_down),
                contentDescription = "${term.title} 펼치기",
                tint = FlintTheme.colors.gray400,
                modifier = Modifier
                    .rotate(if (isExpanded) 180f else 0f)
                    .flintIconClickable(onClick = onExpandClick),
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = FlintTheme.colors.gray800,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp),
            ) {
                // 전문(term.content)은 "자세히 보기" 웹뷰에서 보여주고, 여기에는 안내 문구만 노출한다.
                Text(
                    text = TermGuides.summaryOf(term.type) ?: term.content,
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.white,
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "자세히 보기",
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.primary200,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.End)
                        .flintIconClickable(onClick = onDetailClick),
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingTermsScreenPreview() {
    FlintTheme {
        OnboardingTermsScreen(
            termsUiState = OnboardingTermsUiState(
                termsState = UiState.Success(
                    listOf(
                        TermModel(
                            id = "1",
                            type = "SERVICE",
                            version = 1,
                            title = "서비스 이용약관",
                            content = "본 약관은 서비스 이용과 관련한 기본적인 권리·의무 및 책임사항을 규정합니다.",
                            required = true,
                            activeAt = "2026-05-13T10:48:54.554Z",
                        ),
                        TermModel(
                            id = "2",
                            type = "PRIVACY",
                            version = 1,
                            title = "개인정보 처리 방침",
                            content = "서비스 제공을 위해 개인정보를 수집·이용합니다.",
                            required = true,
                            activeAt = "2026-05-13T10:48:54.554Z",
                        ),
                        TermModel(
                            id = "3",
                            type = "MARKETING",
                            version = 1,
                            title = "마케팅 정보 수신 동의",
                            content = "이벤트 및 마케팅 정보를 수신합니다.",
                            required = false,
                            activeAt = "2026-05-13T10:48:54.554Z",
                        ),
                    )
                )
            ),
            onBackClick = {},
            onCheckClick = {},
            onAllCheckClick = {},
            onExpandClick = {},
            onAgreeClick = {},
        )
    }
}
