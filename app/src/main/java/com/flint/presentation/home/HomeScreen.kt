package com.flint.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.home.component.HomeBanner

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateToCollectionCreate: () -> Unit,
) {
    HomeScreen(
        userName = "종우",
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun HomeScreen(
    userName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(FlintTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                FlintLogoTopAppbar()
            }

            item {
                Spacer(Modifier.height(5.dp))

                HomeBanner(
                    userName = userName
                )
            }

            item {
                Spacer(Modifier.height(48.dp))


            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    FlintTheme {
        HomeScreen(userName = "종우",)
    }
}