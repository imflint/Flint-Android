package com.flint.android.core.designsystem.interaction

import android.os.SystemClock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.semantics.Role
import kotlinx.coroutines.launch

/**
 * Flint 앱 전역 프레스 피드백 규격.
 *
 * 누르면 살짝 줄어들면서(scale) 어두워지고(dim), 떼면 spring 으로 복귀한다.
 * scale 은 요소 크기에 따라 체감이 달라서 버튼 / 카드 / 아이콘용 값을 따로 둔다.
 */
object FlintPressDefaults {
    /** 큰 버튼처럼 눌림이 확실히 보여야 하는 요소 */
    const val BUTTON_SCALE = 0.96f

    /** 리스트·카드처럼 면적이 넓어서 많이 줄면 어색한 요소 */
    const val CARD_SCALE = 0.98f

    /** 아이콘처럼 작아서 같은 비율로는 축소가 잘 안 보이는 요소 */
    const val ICON_SCALE = 0.90f

    /** 축소 없이 딤만 적용 */
    const val NO_SCALE = 1f

    const val DIM_ALPHA = 0.12f

    /** 더블탭으로 같은 화면에 두 번 진입하거나 API 가 두 번 호출되는 것을 막는 간격 */
    const val THROTTLE_MILLIS = 300L

    /** 누를 때는 지연 없이 붙어야 해서 짧은 tween */
    val pressDownSpec: AnimationSpec<Float> =
        tween(durationMillis = 90, easing = LinearOutSlowInEasing)

    /** 뗄 때는 살짝 튕기며 복귀 */
    val pressReleaseSpec: AnimationSpec<Float> =
        spring(dampingRatio = 0.55f, stiffness = 800f)

    val dimSpec: AnimationSpec<Float> =
        tween(durationMillis = 120, easing = LinearOutSlowInEasing)

    /** FlintTheme 이 LocalIndication 으로 제공하는 기본 인디케이션 */
    val dimIndication: Indication = FlintDimIndication()
}

/**
 * 눌린 동안 콘텐츠 위에 반투명 딤을 덮는 [Indication].
 *
 * 딤은 사각형으로 그려지므로 **호출부에서 `clip()` 이 먼저 적용되어 있어야** 모서리가 둥글게 나온다.
 * (`clip` → `background` → `clickable` 순서를 지킬 것)
 */
@Stable
class FlintDimIndication(
    private val color: Color = Color.Black,
    private val maxAlpha: Float = FlintPressDefaults.DIM_ALPHA,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        DimNode(interactionSource, color, maxAlpha)

    override fun equals(other: Any?): Boolean =
        this === other || (other is FlintDimIndication && other.color == color && other.maxAlpha == maxAlpha)

    override fun hashCode(): Int = 31 * color.hashCode() + maxAlpha.hashCode()

    private class DimNode(
        private val interactionSource: InteractionSource,
        private val color: Color,
        private val maxAlpha: Float,
    ) : Modifier.Node(), DrawModifierNode {
        private val alpha = Animatable(0f)

        override fun onAttach() {
            coroutineScope.launch {
                // 멀티터치로 Press 가 겹칠 수 있어서 개수를 세고, 하나라도 남아 있으면 눌린 상태로 본다.
                val presses = mutableListOf<PressInteraction.Press>()

                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> presses.add(interaction)
                        is PressInteraction.Release -> presses.remove(interaction.press)
                        is PressInteraction.Cancel -> presses.remove(interaction.press)
                        else -> return@collect
                    }

                    val target = if (presses.isNotEmpty()) maxAlpha else 0f
                    // animateTo 는 이전 애니메이션을 취소하므로 별도 코루틴에서 실행해 collect 를 막지 않는다.
                    launch { alpha.animateTo(target, FlintPressDefaults.dimSpec) }
                }
            }
        }

        override fun ContentDrawScope.draw() {
            drawContent()

            val current = alpha.value
            if (current > 0f) {
                drawRect(color = color, alpha = current)
            }
        }
    }
}

/**
 * 눌린 동안 [pressedScale] 배로 축소한다.
 *
 * `graphicsLayer` 는 자기보다 **뒤에 오는** 모디파이어의 그리기에 적용되므로,
 * `clip`/`background` 보다 **앞에** 붙여야 모서리와 배경이 함께 줄어든다.
 */
@Composable
fun Modifier.pressScale(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
    pressedScale: Float = FlintPressDefaults.BUTTON_SCALE,
): Modifier {
    if (pressedScale == FlintPressDefaults.NO_SCALE) return this

    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(FlintPressDefaults.NO_SCALE) }

    LaunchedEffect(isPressed, enabled, pressedScale) {
        if (isPressed && enabled) {
            scale.animateTo(pressedScale, FlintPressDefaults.pressDownSpec)
        } else {
            scale.animateTo(FlintPressDefaults.NO_SCALE, FlintPressDefaults.pressReleaseSpec)
        }
    }

    return graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}

/**
 * 딤 인디케이션 + 중복 클릭 방지가 적용된 clickable. 축소는 하지 않는다.
 *
 * 딤이 모서리를 따라 잘리려면 `clip()` **뒤에** 와야 하고, 축소는 `clip()` **앞에** 와야 해서
 * 배경·모서리를 가진 컴포넌트에서는 [pressScale] 과 짝지어 아래 순서로 쓴다.
 *
 * ```
 * modifier
 *     .pressScale(interactionSource, enabled)   // 축소
 *     .clip(shape)
 *     .background(color)
 *     .pressClickable(interactionSource) { ... } // 딤 + 클릭
 * ```
 *
 * 배경도 모서리도 없는 요소라면 둘을 한 번에 붙인 [flintClickable] 을 쓰면 된다.
 */
@Composable
fun Modifier.pressClickable(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    role: Role? = null,
    indication: Indication? = FlintPressDefaults.dimIndication,
    onClick: () -> Unit,
): Modifier =
    clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        role = role,
        onClick = rememberThrottledClick(throttleMillis, onClick),
    )

/**
 * 스케일 축소 + 딤 + 중복 클릭 방지가 모두 적용된 클릭 모디파이어.
 *
 * 자체 `clip`/`background` 가 없는 요소(아이콘, 텍스트, 이미 잘린 카드 내부 영역 등)용이다.
 * 둥근 배경을 직접 그리는 컴포넌트라면 [pressScale] + [pressClickable] 조합을 써야
 * 딤이 모서리를 넘어가지 않는다.
 *
 * @param pressedScale [FlintPressDefaults] 의 BUTTON/CARD/ICON_SCALE 중 요소 크기에 맞는 값
 * @param throttleMillis 0 이면 스로틀 없이 매 탭마다 호출한다
 */
@Composable
fun Modifier.flintClickable(
    enabled: Boolean = true,
    pressedScale: Float = FlintPressDefaults.CARD_SCALE,
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    role: Role? = null,
    indication: Indication? = FlintPressDefaults.dimIndication,
    onClick: () -> Unit,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }

    return this
        .pressScale(interactionSource, enabled, pressedScale)
        .pressClickable(
            interactionSource = interactionSource,
            enabled = enabled,
            throttleMillis = throttleMillis,
            role = role,
            indication = indication,
            onClick = onClick,
        )
}

/**
 * 아이콘 버튼용. 축소만 주고 딤은 쓰지 않는다.
 *
 * 아이콘은 보통 `clip` 된 배경이 없어서 딤이 아이콘 주변에 사각형으로 그대로 드러난다.
 * 대신 작은 요소라 축소 비율을 [FlintPressDefaults.ICON_SCALE] 로 크게 잡는다.
 */
@Composable
fun Modifier.flintIconClickable(
    enabled: Boolean = true,
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    onClick: () -> Unit,
): Modifier =
    flintClickable(
        enabled = enabled,
        pressedScale = FlintPressDefaults.ICON_SCALE,
        throttleMillis = throttleMillis,
        role = Role.Button,
        indication = null,
        onClick = onClick,
    )

/**
 * 카드·리스트 행용. 축소만 주고 딤은 쓰지 않는다.
 *
 * 이 앱은 화면 배경이 거의 검정(#121212)이라 그 위의 카드에 검정 딤을 덮어도 눈에 띄지 않는다.
 * 딤은 채워진 배경을 가진 버튼에서만 의미가 있어서, 행에서는 축소를 주된 신호로 쓴다.
 */
@Composable
fun Modifier.flintCardClickable(
    enabled: Boolean = true,
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    onClick: () -> Unit,
): Modifier =
    flintClickable(
        enabled = enabled,
        pressedScale = FlintPressDefaults.CARD_SCALE,
        throttleMillis = throttleMillis,
        role = Role.Button,
        indication = null,
        onClick = onClick,
    )

/**
 * 시각적 피드백 없이 중복 클릭만 막는 클릭 모디파이어.
 *
 * 피드백을 일부러 빼야 하는 곳(스크림 탭으로 닫기 등)에만 쓰고,
 * 일반적인 버튼·카드·아이콘에는 [flintClickable] / [flintCardClickable] / [flintIconClickable] 을 쓴다.
 */
@Composable
fun Modifier.flintNoFeedbackClickable(
    enabled: Boolean = true,
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier =
    flintClickable(
        enabled = enabled,
        pressedScale = FlintPressDefaults.NO_SCALE,
        throttleMillis = throttleMillis,
        role = role,
        indication = null,
        onClick = onClick,
    )

/**
 * [throttleMillis] 안에 들어온 두 번째 이후 호출을 버리는 콜백을 만든다.
 *
 * 화면 전환이나 네트워크 요청처럼 두 번 실행되면 안 되는 동작을 감싸기 위한 것으로,
 * 시간 기준은 기기 시각 변경에 영향받지 않도록 [SystemClock.elapsedRealtime] 을 쓴다.
 */
@Composable
fun rememberThrottledClick(
    throttleMillis: Long = FlintPressDefaults.THROTTLE_MILLIS,
    onClick: () -> Unit,
): () -> Unit {
    if (throttleMillis <= 0L) return onClick

    val currentOnClick by rememberUpdatedState(onClick)
    val lastClickedAt = remember { mutableLongStateOf(0L) }

    return remember(throttleMillis) {
        {
            val now = SystemClock.elapsedRealtime()
            if (now - lastClickedAt.longValue >= throttleMillis) {
                lastClickedAt.longValue = now
                currentOnClick()
            }
        }
    }
}
