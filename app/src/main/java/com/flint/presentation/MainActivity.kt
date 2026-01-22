package com.flint.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.flint.R
import com.flint.core.designsystem.component.modal.OneButtonModal
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.data.di.interceptor.NetworkError
import com.flint.data.di.interceptor.NetworkErrorManager
import com.flint.presentation.main.MainScreen
import com.flint.presentation.main.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkErrorManager: NetworkErrorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlintTheme {
                var networkError by remember { mutableStateOf<NetworkError?>(null) }

                LaunchedEffect(Unit) {
                    networkErrorManager.networkError.collect { error ->
                        networkError = error
                    }
                }

                val navigator = rememberMainNavigator()
                MainScreen(
                    navigator = navigator,
                )

                networkError?.let { error ->
                    OneButtonModal(
                        icon = R.drawable.ic_gradient_none,
                        title = "문제가 발생했어요",
                        message = "잠시 후 다시 시도해주세요",
                        buttonText = "다시 시작하기",
                        onConfirm = {
                            restartApplication()
                        },
                        onDismiss = {}
                    )
                }
            }
        }
    }

    private fun restartApplication() {
        val packageManager: PackageManager = this.packageManager
        val intent = packageManager.getLaunchIntentForPackage(this.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        this.startActivity(mainIntent)
        exitProcess(0)
    }
}
