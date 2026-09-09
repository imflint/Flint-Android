package com.flint.android.core.analytics

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * 화면 진입 이벤트를 컴포저블에서 바로 보낼 수 있도록 [AnalyticsTracker] 를 트리에 흘려준다.
 *
 * 온보딩처럼 여러 화면이 ViewModel 하나를 공유하는 구간에서는 화면별 진입 시점을
 * ViewModel 로 표현할 수 없어서, 진입 이벤트만 UI 계층에서 처리한다.
 * 완료·성공처럼 비즈니스 로직에 붙는 이벤트는 ViewModel 에서 보낸다.
 */
val LocalAnalyticsTracker =
    staticCompositionLocalOf<AnalyticsTracker> {
        error("LocalAnalyticsTracker 가 제공되지 않았습니다. FlintTheme 하위에서 사용하세요.")
    }

/**
 * 화면 진입 이벤트를 이 컴포저블이 살아 있는 동안 한 번만 보낸다.
 *
 * 화면 회전이나 프로세스 종료 후 복귀로 컴포지션이 다시 만들어져도 중복 전송되지 않도록
 * 전송 여부를 [rememberSaveable] 로 남긴다. 뒤로 갔다가 다시 들어오는 경우는
 * 컴포저블이 새로 생성되므로 의도대로 다시 집계된다.
 */
@Composable
fun TrackScreenView(event: FlintEvent) {
    val tracker = LocalAnalyticsTracker.current
    var isTracked by rememberSaveable(event.eventName) { mutableStateOf(false) }

    LaunchedEffect(event.eventName) {
        if (isTracked) return@LaunchedEffect
        tracker.track(event)
        isTracked = true
    }
}

/**
 * 화면에 머문 시간을 재고, 벗어날 때 [onExit] 으로 초 단위 값을 넘긴다.
 *
 * 기획 정의상 탭 전환·상세 진입·백그라운드 전환을 모두 이탈로 본다.
 * 백그라운드로 나간 순간 이탈로 처리하므로 백그라운드에 머문 시간은 애초에 누적되지 않고,
 * 돌아오면 새 체류로 다시 센다.
 *
 * 시간은 기기 시각 변경에 영향받지 않도록 [SystemClock.elapsedRealtime] 을 쓴다.
 * 한 번의 체류에 대해 이벤트가 두 번 나가지 않도록 전송 여부를 함께 관리한다.
 *
 * 앱이 강제 종료되면 이탈을 알릴 기회가 없다. SDK 가 로컬 큐에 쌓아 다음 실행에 보내주지만,
 * 앱을 다시 열지 않는 사용자의 기록은 남지 않는다.
 */
@Composable
fun TrackDwellTime(onExit: (durationSec: Long) -> Unit) {
    val currentOnExit by rememberUpdatedState(onExit)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        var startedAt = SystemClock.elapsedRealtime()
        var isSent = false

        fun sendOnce() {
            if (isSent) return
            isSent = true
            currentOnExit((SystemClock.elapsedRealtime() - startedAt) / MILLIS_PER_SECOND)
        }

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> sendOnce()
                Lifecycle.Event.ON_START -> {
                    // 돌아왔으면 새로운 체류의 시작이다.
                    startedAt = SystemClock.elapsedRealtime()
                    isSent = false
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // 탭 전환·상세 진입·뒤로가기로 화면을 벗어난 경우.
            sendOnce()
        }
    }
}

private const val MILLIS_PER_SECOND = 1_000L

/**
 * 목록 항목이 화면에 충분히 오래 보였을 때 노출로 집계한다.
 *
 * 기준은 기획 확정값이다 — 항목 높이의 [minVisibleFraction] 이상이 화면에 드러난 채로
 * [minVisibleMillis] 이상 유지되어야 한다. 스크롤로 빠르게 지나친 항목은 제외된다.
 *
 * 한 번 보낸 항목은 [alreadyTracked] 로 걸러 같은 방문 안에서 다시 보내지 않는다.
 * 위아래로 오르내리며 같은 항목을 여러 번 지나쳐도 한 번만 집계된다.
 *
 * 가시 비율은 잘려서 실제로 보이는 높이를 항목 전체 높이로 나눠 구한다.
 */
@Composable
fun Modifier.onItemImpression(
    key: Any,
    alreadyTracked: (Any) -> Boolean,
    onImpression: (Any) -> Unit,
    minVisibleFraction: Float = 0.5f,
    minVisibleMillis: Long = 1_000L,
): Modifier {
    val currentOnImpression by rememberUpdatedState(onImpression)
    val currentAlreadyTracked by rememberUpdatedState(alreadyTracked)
    var isSufficientlyVisible by remember(key) { mutableStateOf(false) }

    LaunchedEffect(key, isSufficientlyVisible) {
        if (!isSufficientlyVisible || currentAlreadyTracked(key)) return@LaunchedEffect
        // 이 시간이 지나기 전에 화면에서 벗어나면 코루틴이 취소되어 집계되지 않는다.
        delay(minVisibleMillis)
        if (!currentAlreadyTracked(key)) currentOnImpression(key)
    }

    return onGloballyPositioned { coordinates ->
        val height = coordinates.size.height
        val visibleHeight = coordinates.boundsInWindow().height
        isSufficientlyVisible = height > 0 && visibleHeight / height >= minVisibleFraction
    }
}
