package com.flint.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.flint.presentation.main.component.MainBottomBar
import com.flint.presentation.main.component.MainTab
import com.flint.presentation.main.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen() {
    val mainNavigator = rememberMainNavigator()
    val currentTab = mainNavigator.currentTab

    Scaffold(
        bottomBar = {
        }
    ) { innerPadding ->
        //TODO: NavHost 연결
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "MainScreen",
            )
        }
    }
}