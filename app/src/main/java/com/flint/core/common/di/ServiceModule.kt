package com.flint.core.common.di

import com.flint.data.api.FlintApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideFlintApi(retrofit: Retrofit): FlintApi = retrofit.create(FlintApi::class.java)
}