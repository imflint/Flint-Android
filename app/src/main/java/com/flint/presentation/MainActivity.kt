package com.flint.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.main.MainScreen
import com.flint.presentation.main.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlintTheme {
                val navigator = rememberMainNavigator()
                MainScreen(
                    navigator = navigator
                )
            }
        }
    }
}
