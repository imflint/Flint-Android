package com.flint.android.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.presentation.onboarding.component.TermsBlock
import com.flint.android.presentation.onboarding.component.parseTermsMarkdown

@Composable
fun OnboardingTermsDetailRoute(
    paddingValues: PaddingValues,
    termId: String,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel,
) {
    val termsUiState by viewModel.termsUiState.collectAsStateWithLifecycle()
    val term = (termsUiState.termsState as? UiState.Success)?.data?.find { it.id == termId }

    OnboardingTermsDetailScreen(
        title = term?.title.orEmpty(),
        content = term?.content.orEmpty(),
        onBackClick = navigateUp,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingTermsDetailScreen(
    title: String,
    content: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val blocks = remember(content) { parseTermsMarkdown(content) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(onClick = onBackClick, title = title)

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(blocks) { block ->
                when (block) {
                    is TermsBlock.Heading -> {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = block.text,
                            style = FlintTheme.typography.body1M16,
                            color = FlintTheme.colors.white,
                        )
                    }

                    is TermsBlock.Paragraph -> Text(
                        text = block.text,
                        style = FlintTheme.typography.body2R14,
                        color = FlintTheme.colors.gray200,
                    )

                    is TermsBlock.ListItem -> Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = block.marker,
                            style = FlintTheme.typography.body2R14,
                            color = FlintTheme.colors.gray200,
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = block.text,
                            style = FlintTheme.typography.body2R14,
                            color = FlintTheme.colors.gray200,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingTermsDetailScreenPreview() {
    FlintTheme {
        OnboardingTermsDetailScreen(
            title = "서비스 이용약관",
            content = """
                ### **제1조. 목적**

                본 이용약관은 "Flint"의 서비스 이용에 관한 사항을 규정합니다.

                ### 제2조. 약관의 게시와 효력, 개정

                1. 사이트는 서비스의 가입 과정에 본 약관을 게시합니다.
                2. 사이트는 관련법에 위배되지 않는 범위에서 본 약관을 변경할 수 있습니다.
            """.trimIndent(),
            onBackClick = {},
        )
    }
}
