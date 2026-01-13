package com.flint.core.common.di

import com.flint.data.repositoryImpl.DefaultFlintRepository
import com.flint.domain.repository.FlintRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFlintRepository(defaultFlintRepository: DefaultFlintRepository): FlintRepository
}
