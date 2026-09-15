package com.flint.android.core.common.extension

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect as ComposeRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.interaction.flintNoFeedbackClickable

/**
 * 시각적 피드백 없이 클릭만 받는다.
 *
 * 이제는 중복 클릭 방지가 함께 걸린 [flintNoFeedbackClickable] 로 위임한다.
 * 새로 작성하는 코드에서 버튼·카드·아이콘처럼 눌림이 보여야 하는 요소에는
 * [com.flint.android.core.designsystem.interaction.flintClickable] 을 쓸 것.
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

@Composable
fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
) = composed {
    val density = LocalDensity.current

    // dropShadow와 동일하게 Paint/BlurMaskFilter를 remember (매 프레임 재할당 방지)
    val shadowPaint = remember(color, blur) {
        Paint().apply {
            this.color = color
            val blurPx = with(density) { blur.toPx() }
            if (blurPx > 0f) {
                this.asFrameworkPaint().maskFilter =
                    BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
        }
    }

    val maskPaint = remember {
        Paint().apply {
            this.color = Color.Black
            blendMode = BlendMode.DstIn
        }
    }

    // drawBehind는 콘텐츠보다 먼저 그려져서, 그림자가 텍스트를 덮지 않는다
    drawBehind {
        if (size.minDimension <= 0f) return@drawBehind

        val outline = shape.createOutline(size, layoutDirection, this)

        val holePath = Path().apply {
            fillType = PathFillType.EvenOdd
            addRect(
                ComposeRect(
                    -size.width,
                    -size.height,
                    size.width * 2f,
                    size.height * 2f,
                ),
            )
            addOutline(outline)
        }

        drawIntoCanvas { canvas ->
            canvas.saveLayer(ComposeRect(Offset.Zero, size), Paint())

            val dx = offsetX.toPx()
            val dy = offsetY.toPx()
            canvas.translate(dx, dy)
            canvas.drawPath(holePath, shadowPaint)
            canvas.translate(-dx, -dy)

            canvas.drawOutline(outline, maskPaint)

            canvas.restore()
        }
    }
}
