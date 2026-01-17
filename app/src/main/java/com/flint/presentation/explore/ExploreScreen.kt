package com.flint.presentation.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.data.model.content.ContentModel
import com.flint.core.common.type.OttType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
) {
//    ExploreScreen(
//        modifier = Modifier.padding(paddingValues),
//    )
}

@Composable
private fun ExploreScreen(
    contents: ImmutableList<ContentModel>,
    onWatchCollectionButtonClick: (collectionId: String) -> Unit,
    onMakeCollectionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageCount: Int = contents.size
    val pagerState: PagerState = rememberPagerState(pageCount = { pageCount + 1 })

    Column(
        modifier
            .run {
                if (pagerState.currentPage < pageCount) {
                    background(FlintTheme.colors.background)
                } else {
                    background(
                        FlintTheme.colors.gradient900,
                    )
                }
            }.fillMaxSize(),
    ) {
        FlintLogoTopAppbar(backgroundColor = Color.Transparent)

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page: Int ->
            val content: ContentModel? = contents.getOrNull(page)

            if (content != null) {
                ExplorePageItem(
                    imageUrl = content.posterImage,
                    id = content.contentId.toString(),
                    title = content.title,
                    description = content.description,
                    onButtonClick = onWatchCollectionButtonClick,
                )
            } else {
                ExploreEndPage(
                    onButtonClick = onMakeCollectionButtonClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ExplorePageItem(
    imageUrl: String,
    id: String,
    title: String,
    description: String,
    onButtonClick: (collectionId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier =
                Modifier
                    .background(brush = FlintTheme.colors.imgBlur)
                    .fillMaxWidth()
                    .aspectRatio(389f / 400f),
        )

        Column(modifier.padding(horizontal = 16.dp)) {
            Text(
                text = title,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = description,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body1R16,
            )

            Spacer(Modifier.height(28.dp))

            FlintLargeButton(
                text = "이 컬렉션 보러가기",
                state = FlintButtonState.ColorOutline,
                onClick = { onButtonClick(id) },
                modifier =
                    Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ExplorePageItemPreview() {
    FlintTheme {
        ExplorePageItem(
            imageUrl = "https://buly.kr/G3Edbfu",
            id = "",
            title = "너의 모든 것",
            description =
                """
                뉴욕의 서점 매니저이자 반듯한 독서가, 조. 
                그가 대학원생 벡을 만나 한눈에 반한다. 
                하지만 훈훈했던 그의 첫인상은 잠시일 뿐, 
                감추어진 조의 뒤틀린 이면이 드러난다.
                """.trimIndent(),
            onButtonClick = {},
        )
    }
}

@Composable
private fun ExploreEndPage(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(FlintTheme.colors.gradient900)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_gradient_pencil),
            contentDescription = null,
        )

        Spacer(Modifier.height(47.dp))

        Text(
            text = "지금 뜨는 추천을 모두 살펴봤어요",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head1Sb22,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "마음에 남는 작품들로\n나만의 컬렉션을 만들어보세요!",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))

        FlintLargeButton(
            text = "컬렉션 만들러 가기",
            state = FlintButtonState.Able,
            onClick = onButtonClick,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ExploreEndPagePreview() {
    FlintTheme {
        ExploreEndPage(
            {},
        )
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    FlintTheme {
        ExploreScreen(
            contents =
                List(10) {
                    ContentModel(
                        contentId = 0,
                        title = "너의 모든 것",
                        year = 2000,
                        posterImage = "https://buly.kr/G3Edbfu",
                        description =
                            """
                            뉴욕의 서점 매니저이자 반듯한 독서가, 조. 
                            그가 대학원생 벡을 만나 한눈에 반한다. 
                            하지만 훈훈했던 그의 첫인상은 잠시일 뿐, 
                            감추어진 조의 뒤틀린 이면이 드러난다.
                            """.trimIndent(),
                        ottSimpleList =
                            listOf(
                                OttType.Netflix,
                                OttType.Disney,
                                OttType.Tving,
                                OttType.Coupang,
                                OttType.Wave,
                                OttType.Watcha,
                            ),
                    )
                }.toImmutableList(),
            onWatchCollectionButtonClick = {},
            onMakeCollectionButtonClick = {},
            modifier =
                Modifier
                    .padding(PaddingValues())
                    .systemBarsPadding()
                    .navigationBarsPadding(),
        )
    }
}
