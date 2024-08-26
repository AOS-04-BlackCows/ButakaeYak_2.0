package com.example.yactong.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindModule {
//    @Binds
//    @ViewModelScoped
//    abstract fun provideAppRepository(impl: AppRepositoryImpl): AppRepository
}