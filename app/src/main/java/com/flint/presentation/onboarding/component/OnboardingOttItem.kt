package com.flint.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.OttType

@Composable
fun OnboardingOttItem(
    ottType: OttType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // OTT 로고 영역
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            // OTT 로고 이미지
            Image(
                painter = painterResource(ottType.imageRes),
                contentDescription = null,
            )

            // 선택 시 어두운 오버레이
            if (isSelected) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(FlintTheme.colors.overlay),
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_onboarding_film_check),
                    contentDescription = "선택됨",
                    tint = FlintTheme.colors.white,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .size(40.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 플랫폼 이름
        Text(
            text = ottType.ottName,
            style = FlintTheme.typography.body1M16,
            color = if (isSelected) FlintTheme.colors.gray300 else FlintTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingOttItemPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .background(FlintTheme.colors.background)
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // 선택된 상태
            OnboardingOttItem(
                ottType = OttType.Netflix,
                isSelected = true,
                onClick = {},
            )

            // 선택되지 않은 상태
            OnboardingOttItem(
                ottType = OttType.Wave,
                isSelected = false,
                onClick = {},
            )

            OnboardingOttItem(
                ottType = OttType.Tving,
                isSelected = false,
                onClick = {},
            )
        }
    }
}
