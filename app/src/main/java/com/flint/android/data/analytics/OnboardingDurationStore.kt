package com.flint.android.data.analytics

import com.flint.android.core.common.util.DataStoreKey
import com.flint.android.data.local.PreferencesManager
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 온보딩 시작 시각을 보관해 `complete_onboarding` 의 소요시간을 계산한다.
 *
 * 온보딩은 약관·작품선택·닉네임까지 여러 화면에 걸쳐 있어 도중에 앱이 종료될 수 있다.
 * 메모리에만 두면 그때 시작 시각이 사라지므로 DataStore 에 남긴다.
 *
 * 시간 기준은 프로세스가 죽어도 이어져야 해서 [System.currentTimeMillis] 를 쓴다.
 * 사용자가 기기 시각을 바꾸면 값이 튈 수 있어, 음수이거나 비상식적으로 큰 값은 버린다.
 */
@Singleton
class OnboardingDurationStore @Inject constructor(
    private val preferencesManager: PreferencesManager,
) {
    /**
     * 온보딩 시작 시각을 기록한다.
     *
     * 이미 기록이 있으면 덮어쓰지 않는다. 온보딩 도중 앱이 종료됐다가 다시 들어온 경우
     * 처음 시작한 시점을 유지해야 실제 소요시간이 나오기 때문이다.
     */
    suspend fun startIfAbsent() {
        if (readStartedAt() != null) return
        preferencesManager.saveString(DataStoreKey.ONBOARDING_STARTED_AT, System.currentTimeMillis().toString())
    }

    /** 시작 시각부터 지금까지의 초. 기록이 없거나 값이 이상하면 null 을 반환한다. */
    suspend fun elapsedSecondsOrNull(): Long? {
        val startedAt = readStartedAt() ?: return null
        val elapsedMillis = System.currentTimeMillis() - startedAt

        if (elapsedMillis < 0 || elapsedMillis > MAX_REASONABLE_DURATION_MILLIS) {
            Timber.w("온보딩 소요시간이 비정상이라 전송하지 않습니다: ${elapsedMillis}ms")
            return null
        }
        return elapsedMillis / MILLIS_PER_SECOND
    }

    suspend fun clear() {
        preferencesManager.removeString(DataStoreKey.ONBOARDING_STARTED_AT)
    }

    private suspend fun readStartedAt(): Long? =
        preferencesManager
            .getString(DataStoreKey.ONBOARDING_STARTED_AT)
            .first()
            .toLongOrNull()
}

private const val MILLIS_PER_SECOND = 1_000L

/** 하루를 넘긴 값은 기기 시각 변경이나 며칠 뒤 재개로 보고 지표에서 제외한다. */
private const val MAX_REASONABLE_DURATION_MILLIS = 24 * 60 * 60 * 1_000L
