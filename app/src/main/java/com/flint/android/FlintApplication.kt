package com.flint.android

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.flint.android.core.analytics.AnalyticsTracker
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FlintApplication : Application() {
    /**
     * 세션이 앱 실행 시점부터 잡히도록 여기서 미리 주입받아 SDK 를 초기화한다.
     * 첫 이벤트가 발생할 때까지 미루면 그 전 구간이 세션에서 빠진다.
     */
    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    // Hilt 의 필드 주입은 super.onCreate() 안에서 일어난다.
    // Timber 를 onCreate 에서 심으면 주입 도중 남긴 로그가 트리 없이 버려지므로
    // 그보다 먼저 호출되는 attachBaseContext 에서 심는다.
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initTimber()
    }

    override fun onCreate() {
        super.onCreate()

        setDayMode()
        initKakaoSdk()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}
