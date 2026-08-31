package com.flint.android.core.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

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
