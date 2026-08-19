package com.flint.android.data.di

import com.flint.android.data.api.AuthApi
import com.flint.android.data.api.BookmarkApi
import com.flint.android.data.api.CollectionApi
import com.flint.android.data.api.ContentApi
import com.flint.android.data.api.ExplorationApi
import com.flint.android.data.api.HomeApi
import com.flint.android.data.api.SearchApi
import com.flint.android.data.api.StorageApi
import com.flint.android.data.api.TermsApi
import com.flint.android.data.api.UserApi
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
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideBookmarkApi(retrofit: Retrofit): BookmarkApi = retrofit.create(BookmarkApi::class.java)

    @Provides
    @Singleton
    fun provideCollectionApi(retrofit: Retrofit): CollectionApi = retrofit.create(CollectionApi::class.java)

    @Provides
    @Singleton
    fun provideContentApi(retrofit: Retrofit): ContentApi = retrofit.create(ContentApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi = retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideStorageApi(retrofit: Retrofit): StorageApi = retrofit.create(StorageApi::class.java)

    @Provides
    @Singleton
    fun provideTermsApi(retrofit: Retrofit): TermsApi = retrofit.create(TermsApi::class.java)

    @Provides
    @Singleton
    fun provideExplorationApi(retrofit: Retrofit): ExplorationApi = retrofit.create(ExplorationApi::class.java)
}
