package com.blackcows.butakaeyak.di

import com.blackcows.butakaeyak.data.repository.impl.LocalUtilsRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.UserRepositoryImpl
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonBindModule {

    @Binds
    @Singleton
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideLocalUtilsRepository(impl: LocalUtilsRepositoryImpl): LocalUtilsRepository

}