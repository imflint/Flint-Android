package com.flint.presentation.splash

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.sampleSaveData()
    }

    val prefData by viewModel.prefData.collectAsStateWithLifecycle("")

    SplashScreen()
}

@Composable
fun SplashScreen() {
}
