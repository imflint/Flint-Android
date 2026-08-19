package com.flint.android.data.di

import android.content.Context
import com.flint.android.BuildConfig
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.data.analytics.AmplitudeAnalyticsTracker
import com.flint.android.data.analytics.NoOpAnalyticsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsTracker(
        @ApplicationContext context: Context,
    ): AnalyticsTracker =
        if (BuildConfig.AMPLITUDE_API_KEY.isBlank()) {
            Timber.w("amplitude.api.key 가 없어 분석 이벤트를 전송하지 않습니다. local.properties 를 확인하세요.")
            NoOpAnalyticsTracker()
        } else {
            AmplitudeAnalyticsTracker(context = context, apiKey = BuildConfig.AMPLITUDE_API_KEY)
        }
}
