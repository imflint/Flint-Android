package com.flint.android.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.component.image.ProfileImage
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.collection.CollectionItemModel

@Composable
fun RecommendCollectionCard(
    item: CollectionItemModel,
    onItemClick: (id: String) -> Unit,
    isCurrentPage: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isCurrentPage) FlintTheme.colors.primary900 else FlintTheme.colors.gray800
    val midGradient = if (isCurrentPage) FlintTheme.colors.blueGradient else FlintTheme.colors.grayGradient
    val bottomGradient = if (isCurrentPage) FlintTheme.colors.primary400Gradient else FlintTheme.colors.gray800Gradient

    Box(
        modifier = modifier
            .width(270.dp)
            .height(320.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .flintCardClickable { onItemClick(item.id) },
    ) {
        NetworkImage(
            imageUrl = item.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(202.dp),
        )

        Box(
            modifier = Modifier
                .padding(top = 70.dp)
                .fillMaxWidth()
                .height(132.dp)
                .background(midGradient),
        )

        Row(
            modifier = Modifier
                .padding(top = 52.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(brush = FlintTheme.colors.userBadgeGradient)
                .border(width = 0.5.dp, brush = FlintTheme.colors.userBadgeStroke, shape = RoundedCornerShape(16.dp))
                .padding(top = 4.dp, bottom = 4.dp, start = 6.dp, end = 8.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProfileImage(
                imageUrl = item.profileUrl,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = item.nickname,
                style = FlintTheme.typography.caption1R12,
                color = FlintTheme.colors.gray200,
                maxLines = 1,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 35.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = item.title,
                style = FlintTheme.typography.head3Sb18,
                color = FlintTheme.colors.gray50,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            )
            Text(
                text = item.description,
                style = FlintTheme.typography.caption1R12,
                color = FlintTheme.colors.gray200,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .align(Alignment.BottomCenter)
                .background(bottomGradient)
        )
    }
}

@Preview
@Composable
private fun RecommendCollectionCardPreview() {
    FlintTheme {
        RecommendCollectionCard(
            item = CollectionItemModel(
                id = "1",
                thumbnailUrl = null,
                title = "추천 컬렉션 제목",
                description = "추천 컬렉션에 대한 설명입니다. 여러 줄일 경우 어떻게 보이는지 확인하기 위해 길게 작성합니다.",
                nickname = "작성자 닉네임",
                profileUrl = null
            ),
            isCurrentPage = true,
            onItemClick = {}
        )
    }
}
