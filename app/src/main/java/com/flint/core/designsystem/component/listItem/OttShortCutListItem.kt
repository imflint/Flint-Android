package com.flint.core.designsystem.component.listItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography
import com.flint.domain.model.ott.OttListModel
import com.flint.domain.model.ott.OttModel
import com.flint.domain.type.OttType

@Composable
fun OttShortCutListItem(
    ottModel: OttModel,
    onMoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 24.dp)
                .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(OttType.valueOf(ottModel.name).iconRes),
            contentDescription = null,
            modifier = Modifier.size(44.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = OttType.valueOf(ottModel.name).ottName,
            style = FlintTheme.typography.body1Sb16,
            color = FlintTheme.colors.white,
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(FlintTheme.colors.primary400)
                    .clickable {
                        onMoveClick()
                    }.padding(vertical = 7.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "바로 보러가기",
                style = FlintTypography.body2M14,
                color = FlintTheme.colors.white,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOttShortCutListItem() {
    FlintTheme {
        Column {
            OttShortCutListItem(
                ottModel = OttModel(
                    ottId = "",
                    name = "Netflix",
                    logoUrl = "",
                    contentUrl = "",
                ),
                onMoveClick = {},
            )
        }
    }
}
