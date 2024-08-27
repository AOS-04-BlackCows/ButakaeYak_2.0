package com.example.yactong.di

import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.DrugApiService
import com.example.yactong.data.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ProvideModule {
    @Provides
    @ViewModelScoped
    fun provideRetrofitService() : DrugApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl).create(DrugApiService::class.java)
    }
}