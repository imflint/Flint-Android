package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.UserRoleType

@Composable
fun CollectionDetailDescription(
    authorNickname: String,
    authorUserRoleType: UserRoleType,
    createdAt: String,
    collectionContent: String,
    onAuthorNicknameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = authorNickname,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head2Sb20,
                modifier = Modifier.noRippleClickable(
                    onClick = { onAuthorNicknameClick() }
                )
            )

            if (authorUserRoleType == UserRoleType.FLINER) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_qualified),
                    contentDescription = "플리너",
                    tint = Color.Unspecified,
                )
            } else {
                Text(
                    "|",
                    color = FlintTheme.colors.gray200,
                    style = FlintTheme.typography.head3M18,
                )
            }

            Text(
                text = createdAt,
                color = FlintTheme.colors.gray200,
                style = FlintTheme.typography.body2M14,
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = FlintTheme.colors.gray300,
        )

        if (collectionContent.isNotBlank()) {
            Text(
                text = collectionContent,
                color = FlintTheme.colors.gray100,
                style = FlintTheme.typography.body1R16,
            )
        }
    }
}

private data class DescriptionPreviewData(
    val authorNickname: String,
    val authorUserRoleType: UserRoleType,
    val createdAt: String,
    val collectionContent: String,
)

private class DescriptionPreviewProvider : PreviewParameterProvider<DescriptionPreviewData> {
    override val values: Sequence<DescriptionPreviewData> =
        sequenceOf(
            DescriptionPreviewData(
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                createdAt = "2026. 01. 07.",
                collectionContent = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
            ),
            DescriptionPreviewData(
                authorNickname = "일반유저",
                authorUserRoleType = UserRoleType.FLING,
                createdAt = "2026. 01. 15.",
                collectionContent = "한글자 두글자 세글자 네글자 다섯글자 ".repeat(10),
            ),
            DescriptionPreviewData(
                authorNickname = "관리자",
                authorUserRoleType = UserRoleType.ADMIN,
                createdAt = "2026. 01. 01.",
                collectionContent = "공식 추천 컬렉션입니다",
            ),
            DescriptionPreviewData(
                authorNickname = "내용없는유저",
                authorUserRoleType = UserRoleType.FLING,
                createdAt = "2026. 06. 16.",
                collectionContent = "",
            ),
        )
}

@Preview
@Composable
private fun CollectionDetailDescriptionPreview(
    @PreviewParameter(DescriptionPreviewProvider::class) data: DescriptionPreviewData,
) {
    FlintTheme {
        CollectionDetailDescription(
            authorNickname = data.authorNickname,
            authorUserRoleType = data.authorUserRoleType,
            createdAt = data.createdAt,
            collectionContent = data.collectionContent,
            onAuthorNicknameClick = {}
        )
    }
}
