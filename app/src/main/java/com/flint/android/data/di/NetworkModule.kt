package com.flint.android.data.di

import com.flint.android.BuildConfig
import com.flint.android.data.di.interceptor.NetworkErrorInterceptor
import com.flint.android.data.di.interceptor.TokenInterceptor
import com.flint.android.data.di.qualifier.S3OkHttpClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
            prettyPrint = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .apply {
                connectTimeout(20, TimeUnit.SECONDS)
                writeTimeout(20, TimeUnit.SECONDS)
                readTimeout(20, TimeUnit.SECONDS)
                addInterceptor(networkErrorInterceptor)
                addInterceptor(tokenInterceptor)
                addInterceptor(loggingInterceptor)
            }.build()

    @Provides
    @Singleton
    @S3OkHttpClient
    fun provideS3OkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .apply {
                connectTimeout(20, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                readTimeout(20, TimeUnit.SECONDS)
                addInterceptor(loggingInterceptor)
            }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
