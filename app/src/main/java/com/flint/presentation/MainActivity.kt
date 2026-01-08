package com.flint.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.NavHost
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.main.MainScreen
import com.flint.presentation.main.navigation.MainNavHost
import com.flint.presentation.main.navigation.rememberMainNavigator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlintTheme {
                val navigator = rememberMainNavigator()
                MainNavHost(
                    navigator = navigator,
                    paddingValues = PaddingValues()
                )
            }
        }
    }
}