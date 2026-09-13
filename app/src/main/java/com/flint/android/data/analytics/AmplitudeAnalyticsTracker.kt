package com.flint.android.data.analytics

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.core.events.BaseEvent
import com.flint.android.BuildConfig
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.core.analytics.FlintEvent
import timber.log.Timber

/**
 * Amplitude SDK 기반 [AnalyticsTracker] 구현.
 *
 * 세션 추적은 SDK 기본 동작에 맡긴다.
 * 전송은 SDK 가 로컬 큐에 쌓았다가 알아서 보내므로, 네트워크가 없거나 앱이
 * 종료되어도 다음 실행 때 재전송된다.
 */
class AmplitudeAnalyticsTracker(
    context: Context,
    apiKey: String,
) : AnalyticsTracker {
    private val amplitude =
        Amplitude(
            Configuration(
                apiKey = apiKey,
                context = context,
                callback = ::logUploadResult,
            ),
        )

    override fun track(event: FlintEvent) {
        if (BuildConfig.DEBUG) {
            Timber.tag(ANALYTICS_TAG).d("track %s %s", event.eventName, event.properties)
        }
        amplitude.track(event.eventName, event.properties)
    }

    override fun setUserId(userId: String?) {
        if (BuildConfig.DEBUG) {
            Timber.tag(ANALYTICS_TAG).d("setUserId %s", userId)
        }
        amplitude.setUserId(userId)
    }

    override fun reset() {
        if (BuildConfig.DEBUG) {
            Timber.tag(ANALYTICS_TAG).d("reset")
        }
        amplitude.reset()
    }

    /**
     * 업로드 결과를 남긴다.
     *
     * SDK 는 서버가 재시도 불가 응답(400 등)을 주면 이벤트를 버리고 로컬 큐에서도 지운다.
     * 그래서 큐가 비어 있다는 것만으로는 전송 성공을 알 수 없어, 실패를 여기서 드러낸다.
     */
    private fun logUploadResult(
        event: BaseEvent,
        status: Int,
        message: String,
    ) {
        if (status == HTTP_OK) {
            if (BuildConfig.DEBUG) Timber.tag(ANALYTICS_TAG).d("uploaded %s", event.eventType)
        } else {
            Timber.tag(ANALYTICS_TAG).w("upload failed %s status=%d %s", event.eventType, status, message)
        }
    }
}

private const val HTTP_OK = 200

/**
 * 전송 없이 로그만 남기는 [AnalyticsTracker].
 *
 * API 키가 없는 환경(CI, 키를 아직 못 받은 팀원)에서 쓰인다.
 * 키가 비었는데 그대로 SDK 를 띄우면 무의미한 요청이 나가고, 실수로 운영
 * 프로젝트에 테스트 로그가 섞이는 것보다 아무것도 안 보내는 편이 안전하다.
 */
class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun track(event: FlintEvent) {
        Timber.tag(ANALYTICS_TAG).d("(전송 안 함) track %s %s", event.eventName, event.properties)
    }

    override fun setUserId(userId: String?) {
        Timber.tag(ANALYTICS_TAG).d("(전송 안 함) setUserId %s", userId)
    }

    override fun reset() {
        Timber.tag(ANALYTICS_TAG).d("(전송 안 함) reset")
    }
}

private const val ANALYTICS_TAG = "Analytics"
