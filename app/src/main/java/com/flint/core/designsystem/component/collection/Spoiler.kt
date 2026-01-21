package com.flint.core.designsystem.component.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun Spoiler(
    onSpoilClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier
            .background(color = FlintTheme.colors.spoilerBlur)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.blur(10.dp),
        ) {
            content()
        }

        Column(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_gradient_lock),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "스포일러가 포함된 내용이에요",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head3Sb18,
            )

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = onSpoilClick,
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(
                    text = "보기",
                    color = FlintTheme.colors.primary300,
                    style = FlintTheme.typography.head3Sb18,
                )

                Spacer(Modifier.width(4.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = FlintTheme.colors.primary300,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpoilerPreview(
    @PreviewParameter(SpoilerPreviewParameterProvider::class) text: String,
) {
    FlintTheme {
        Spoiler(onSpoilClick = {}) {
            Text(
                text = text,
                style = FlintTheme.typography.body1R16,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 183.dp),
            )
        }
    }
}

class SpoilerPreviewParameterProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> =
        sequenceOf(
            "한줄평: 재밌었다",
            """
            달라진 온도
            -
            같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들
            
            어떻게 이런 연출을 할 수 있는지 감독이 너무 변태같다
            
            """.trimIndent(),
            """
            달라진 온도
            -
            같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들
            
            어떻게 이런 연출을 할 수 있는지 감독이 너무 변태같다
            
            달라진 온도
            -
            같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들
            
            어떻게 이런 연출을 할 수 있는지 감독이 너무 변태같다
            
            달라진 온도
            -
            같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들
            
            어떻게 이런 연출을 할 수 있는지 감독이 너무 변태같다
            
            """.trimIndent(),
        )
}
