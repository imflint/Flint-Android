package com.flint.core.common.di

import com.flint.data.repositoryImpl.DefaultAuthRepository
import com.flint.data.repositoryImpl.DefaultUserRepository
import com.flint.domain.repository.AuthRepository
import com.flint.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository
}
