package com.example.yactong.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ProvideModule {
//    @Provides
//    @ViewModelScoped
//    fun provideRetrofitService() : RetrofitService {
//        return RetrofitClient.getInstance().create(RetrofitService::class.java)
//    }
}