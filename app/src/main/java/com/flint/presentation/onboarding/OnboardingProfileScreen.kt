package com.flint.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.EditProfileImage
import com.flint.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OnboardingProfileRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingFilm: () -> Unit,
) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingProfileScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var nickname by remember { mutableStateOf("") }
    val maxLength = 10

    Column(
        modifier = Modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
                .statusBarsPadding(),
    ) {
        FlintBackTopAppbar(
            onClick = {},
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EditProfileImage(
                imageUrl = "",
                onEditClick = { showBottomSheet = true },
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "플린트에서 사용할 이름을 정해주세요.",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head3M18,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlintBasicTextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    placeholder = "닉네임",
                    value = nickname,
                    maxLength = maxLength,
                    onValueChange = { nickname = it },
                    trailingContent = {
                        Text(
                            text = "${nickname.length}/$maxLength",
                            style = FlintTheme.typography.body1R16,
                            color = FlintTheme.colors.gray300,
                        )
                    },
                )

                FlintBasicButton(
                    text = "확인",
                    state = if (nickname.isNotEmpty()) FlintButtonState.Able else FlintButtonState.Disable,
                    onClick = { },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }

        FlintBasicButton(
            text = "시작하기",
            state = if (nickname.isNotEmpty()) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = {},
            contentPadding = PaddingValues(12.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
        )
    }

    if (showBottomSheet) {
        MenuBottomSheet(
            sheetState = sheetState,
            onDismiss = { showBottomSheet = false },
            menuBottomSheetDataList =
                listOf(
                    MenuBottomSheetData(
                        label = "갤러리에서 선택",
                        clickAction = { },
                    ),
                    MenuBottomSheetData(
                        label = "프로필 사진 삭제",
                        color = FlintTheme.colors.error500,
                        clickAction = { },
                    ),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenPreview() {
    FlintTheme {
        OnboardingProfileScreen()
    }
}
