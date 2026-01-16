package com.flint.core.common.di

import com.flint.data.repositoryImpl.DefaultAuthRepository
import com.flint.domain.repository.AuthRepository
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
    abstract fun bindFlintRepository(defaultFlintRepository: DefaultAuthRepository): AuthRepository
}
