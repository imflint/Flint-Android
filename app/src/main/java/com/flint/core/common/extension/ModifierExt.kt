package com.flint.core.common.extension

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Rect
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.flint.core.designsystem.interaction.flintNoFeedbackClickable

/**
 * 시각적 피드백 없이 클릭만 받는다.
 *
 * 이제는 중복 클릭 방지가 함께 걸린 [flintNoFeedbackClickable] 로 위임한다.
 * 새로 작성하는 코드에서 버튼·카드·아이콘처럼 눌림이 보여야 하는 요소에는
 * [com.flint.core.designsystem.interaction.flintClickable] 을 쓸 것.
 */
@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = flintNoFeedbackClickable(enabled = enabled, onClick = onClick)

@Composable
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 1.dp,
    spread: Dp = 1.dp,
) = composed {
    val density = LocalDensity.current

    val paint =
        remember(color, blur) {
            Paint().apply {
                this.color = color
                val blurPx = with(density) { blur.toPx() }
                if (blurPx > 0f) {
                    this.asFrameworkPaint().maskFilter =
                        BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
        }

    drawBehind {
        val spreadPx = spread.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val shadowWidth = size.width + spreadPx
        val shadowHeight = size.height + spreadPx

        if (shadowWidth <= 0f || shadowHeight <= 0f) return@drawBehind

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetXPx, offsetYPx)
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}

fun Modifier.draw9Patch(
    context: Context,
    @DrawableRes ninePatchRes: Int,
) = this.drawBehind {
    drawIntoCanvas {
        ContextCompat.getDrawable(context, ninePatchRes)?.let { ninePatch ->
            ninePatch.run {
                bounds = Rect(0, 0, size.width.toInt(), size.height.toInt())
                draw(it.nativeCanvas)
            }
        }
    }
}
