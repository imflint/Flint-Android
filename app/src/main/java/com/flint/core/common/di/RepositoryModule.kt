package com.flint.core.common.di

import com.flint.data.repository.AuthRepository
import com.flint.data.repository.UserRepository
import com.flint.data.repositoryImpl.DefaultAuthRepository
import com.flint.data.repositoryImpl.DefaultUserRepository
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
    abstract fun bindAuthRepository(defaultAuthRepository: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository
}
