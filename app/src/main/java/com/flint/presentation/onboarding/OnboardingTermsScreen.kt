package com.flint.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

private data class TermItem(
    val title: String,
    val description: String,
    val detailUrl: String,
)

private val terms = listOf(
    TermItem(
        title = "(필수) 서비스 이용 약관 동의",
        description = "본 약관은 서비스 이용과 관련한 기본적인 권리·의무 및 책임사항을 규정합니다.",
        detailUrl = "",
    ),
    TermItem(
        title = "(필수) 개인정보 처리 방침 동의",
        description = "서비스 제공을 위해 개인정보를 수집·이용합니다.\n" +
            "콘텐츠 추천, 컬렉션 생성 및 공유, 맞춤형 탐색 경험 제공을 위한 이용 기록 및 취향 정보 처리 내용이 포함됩니다.\n\n" +
            "수집 항목: 계정 정보, 취향 정보, 컬렉션 및 콘텐츠 활동, 서비스 이용 기록 등\n\n" +
            "수집 목적: 개인화 추천 제공, 컬렉션 생성 및 공유, 서비스 운영 및 이용자 보호",
        detailUrl = "",
    ),
)

@Composable
fun OnboardingTermsRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToOnboardingContent: () -> Unit,
) {
    OnboardingTermsScreen(
        onBackClick = navigateUp,
        onAgreeClick = navigateToOnboardingContent,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingTermsScreen(
    onBackClick: () -> Unit,
    onAgreeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var checkedStates by remember { mutableStateOf(List(terms.size) { false }) }
    var expandedStates by remember { mutableStateOf(List(terms.size) { false }) }
    val allChecked = checkedStates.all { it }
    val uriHandler = LocalUriHandler.current

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
                        .noRippleClickable {
                            val newState = !allChecked
                            checkedStates = List(terms.size) { newState }
                        }
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

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(16.dp))

                terms.forEachIndexed { index, term ->
                    TermRow(
                        term = term,
                        isChecked = checkedStates[index],
                        isExpanded = expandedStates[index],
                        onCheckClick = {
                            checkedStates = checkedStates.toMutableList().also { it[index] = !it[index] }
                        },
                        onExpandClick = {
                            expandedStates = expandedStates.toMutableList().also { it[index] = !it[index] }
                        },
                        onDetailClick = {
                            if (term.detailUrl.isNotEmpty()) uriHandler.openUri(term.detailUrl)
                        },
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        FlintLargeButton(
            text = "동의하기",
            state = if (allChecked) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = onAgreeClick,
            enabled = allChecked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        )
    }
}

@Composable
private fun TermRow(
    term: TermItem,
    isChecked: Boolean,
    isExpanded: Boolean,
    onCheckClick: () -> Unit,
    onExpandClick: () -> Unit,
    onDetailClick: () -> Unit,
) {
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
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.noRippleClickable(onClick = onCheckClick),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = term.title,
                style = FlintTheme.typography.body1R16,
                color = FlintTheme.colors.white,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_down),
                contentDescription = null,
                tint = FlintTheme.colors.gray400,
                modifier = Modifier
                    .rotate(if (isExpanded) 180f else 0f)
                    .noRippleClickable(onClick = onExpandClick),
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
                Text(
                    text = term.description,
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.gray300,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "자세히 보기",
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.primary200,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.End)
                        .noRippleClickable(onClick = onDetailClick),
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
            onBackClick = {},
            onAgreeClick = {},
        )
    }
}
