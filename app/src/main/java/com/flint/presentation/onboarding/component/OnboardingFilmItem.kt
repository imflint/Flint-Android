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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.flint.R
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintColors
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OnboardingFilmItem(
    imageUrl: String,
    title: String,
    director: String,
    releaseYear: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(100.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // 영화 포스터 영역
        Box(
            modifier =
            Modifier
                .aspectRatio(2f / 3f) // 영화 포스터 2:3 비율 유지
                .clip(RoundedCornerShape(0.dp))
                .clickable { onClick() }
        ) {
            // 영화 포스터 이미지
            NetworkImage(
                imageUrl = imageUrl,
                contentDescription = title ,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 선택 시 어두운 오버레이
            if (isSelected) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(FlintTheme.colors.overlay)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_onboarding_film_check),
                    contentDescription = "선택됨",
                    tint = FlintTheme.colors.white,
                    modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 영화 제목
        Text(
            text = title,
            style = FlintTheme.typography.body1R16,
            color = FlintTheme.colors.white,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis, // 넘치면 ...
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 감독 및 개봉일
        Text(
            text = director,
            style = FlintTheme.typography.caption1R12,
            color = FlintColors.gray300,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = releaseYear,
            style = FlintTheme.typography.caption1R12,
            color = FlintColors.gray300,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun OnboardingFilmItemSelectedPreview() {
    FlintTheme {
        Column(
            modifier =
            Modifier
                .background(FlintColors.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 선택된 상태
            OnboardingFilmItem(
                imageUrl = "https://image.tmdb.org/t/p/w500/ulzhLuWrPK07PqYcvbBt2vWAbqB.jpg",
                title = "김준서김나현임차민김종우박찬미",
                director = "김준서김나현임차민김종우박찬미",
                releaseYear = "2005",
                isSelected = true,
                onClick = {}
            )

            // 선택되지 않은 상태
            OnboardingFilmItem(
                imageUrl = "https://image.tmdb.org/t/p/w500/ulzhLuWrPK07PqYcvbBt2vWAbqB.jpg",
                title = "김준서김나현",
                director = "김준서김나현임차민김종우박찬미",
                releaseYear = "2005",
                isSelected = false,
                onClick = {}
            )
        }
    }
}
