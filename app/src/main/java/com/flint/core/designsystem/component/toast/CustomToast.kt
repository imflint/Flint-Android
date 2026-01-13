package com.flint.core.designsystem.component.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

sealed class ToastType(
    val space: Int,
) {
    data class Success(
        val message: String,
        val bottomSpace: Int = 72,
    ) : ToastType(bottomSpace)

    data class Error(
        val message: String,
        val bottomSpace: Int = 72,
    ) : ToastType(bottomSpace)

    data class Default(
        val message: String,
        val bottomSpace: Int = 72,
    ) : ToastType(bottomSpace)
}

object CustomToastUtil {
    @Composable
    fun SetView(type: ToastType) {
        val message =
            when (type) {
                is ToastType.Success -> type.message
                is ToastType.Error -> type.message
                is ToastType.Default -> type.message
            }

        val icon =
            when (type) {
                is ToastType.Success -> R.drawable.ic_check
                is ToastType.Error -> R.drawable.ic_x
                is ToastType.Default -> null
            }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .background(
                        color = FlintTheme.colors.gray700,
                        shape = RoundedCornerShape(44.dp),
                    ).padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            icon?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp),
                )
            }

            Text(
                text = message,
                style = FlintTheme.typography.body2R14,
                color = FlintTheme.colors.white,
                modifier = Modifier.padding(start = if (icon != null) 8.dp else 0.dp),
            )
        }
    }
}

class CustomToast(
    context: Context,
) : Toast(context) {
    @Composable
    fun MakeText(
        type: ToastType,
        duration: Int = LENGTH_SHORT,
    ) {
        val context = LocalContext.current
        val density = LocalDensity.current
        val views = ComposeView(context)

        views.setContent {
            FlintTheme {
                CustomToastUtil.SetView(type = type)
            }
        }

        views.setViewTreeLifecycleOwner(LocalLifecycleOwner.current)
        views.setViewTreeSavedStateRegistryOwner(LocalSavedStateRegistryOwner.current)
        LocalViewModelStoreOwner.current?.let {
            views.setViewTreeViewModelStoreOwner(it)
        }

        // dp -> 픽셀로 변환
        val yOffsetPx =
            with(density) {
                type.space.dp
                    .toPx()
                    .toInt()
            }

        this.duration = duration
        this.view = views
        this.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, yOffsetPx)
        this.show()
    }
}
