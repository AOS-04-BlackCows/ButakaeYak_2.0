package com.blackcows.butakaeyak.di

import com.algolia.search.saas.Client
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.service.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.service.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.retrofit.service.MedicineInfoService
import com.blackcows.butakaeyak.data.retrofit.service.OCRTextRecApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
class ProvideModule {
    @Provides
    @ViewModelScoped
    fun provideRetrofitService() : DrugApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl).create(DrugApiService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @ViewModelScoped
    fun provideAlgoliaClient(): Client {
        return Client(BuildConfig.ALGORIA_APP_ID, BuildConfig.ALGORIA_SEARCH_KEY)
    }
    
    @Provides
    @ViewModelScoped
    fun provideKakaoApiService() : KakaoApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.KakaoPlaceSearchUrl).create(KakaoApiService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideMedicineInfoApiService() : MedicineInfoService {
        return RetrofitClient.getInstance(ApiBaseUrl.MedicineInfoUrl).create(MedicineInfoService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideGPTService() : OCRTextRecApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.GptUrl).create(OCRTextRecApiService::class.java)
    }
}