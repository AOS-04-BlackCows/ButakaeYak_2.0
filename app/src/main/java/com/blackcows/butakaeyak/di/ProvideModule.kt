package com.blackcows.butakaeyak.di

import com.algolia.search.saas.Client
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.repository.impl.MedicineGroupRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MemoRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MyPharmacyRepositoryImpl
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.service.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.service.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.retrofit.service.MedicineInfoService
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.data.source.firebase.MemoDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMyPharmacyDataSource
import com.blackcows.butakaeyak.data.source.local.LocalMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.local.LocalMyPharmacyDataSource
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import com.blackcows.butakaeyak.domain.repo.MyPharmacyRepository
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Converter
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvideModule {
    @Provides
    //@ViewModelScoped
    fun provideRetrofitService() : DrugApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl).create(DrugApiService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    //@ViewModelScoped
    fun provideAlgoliaClient(): Client {
        return Client(BuildConfig.ALGORIA_APP_ID, BuildConfig.ALGORIA_SEARCH_KEY)
    }
    
    @Provides
    //@ViewModelScoped
    fun provideKakaoApiService() : KakaoApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.KakaoPlaceSearchUrl).create(KakaoApiService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideMedicineInfoApiService() : MedicineInfoService {
        return RetrofitClient.getInstance(ApiBaseUrl.MedicineInfoUrl).create(MedicineInfoService::class.java)
    }
//
//    @Provides
//    fun provideMyPharmacyRepository(
//        localUtilsDataSource: LocalUtilsDataSource,
//        remoteMyPharmacyDataSource: RemoteMyPharmacyDataSource,
//        localMyPharmacyDataSource: LocalMyPharmacyDataSource,
//    ): MyPharmacyRepository {
//        return MyPharmacyRepositoryImpl(
//            localMyPharmacyDataSource = localMyPharmacyDataSource,
//            remoteMyPharmacyDataSource = remoteMyPharmacyDataSource,
//            localUtilsDataSource = localUtilsDataSource
//        )
//    }
//
//    @Provides
//    fun provideMedicineGroupRepository(
//        localUtilsDataSource: LocalUtilsDataSource,
//        remoteMedicineGroupDataSource: RemoteMedicineGroupDataSource,
//        localMedicineGroupDataSource: LocalMedicineGroupDataSource,
//        medicineInfoDataSource: MedicineInfoDataSource
//    ): MedicineGroupRepository {
//        return MedicineGroupRepositoryImpl(
//            localMedicineGroupDataSource = localMedicineGroupDataSource,
//            remoteMedicineGroupDataSource = remoteMedicineGroupDataSource,
//            localUtilsRepository = localUtilsDataSource,
//            medicineDetailDataSource = medicineInfoDataSource
//        )
//    }
//
//    @Provides
//    //@ViewModelScoped
//    fun provideMemoRepository(
//        remoteMedicineGroupDataSource: RemoteMedicineGroupDataSource,
//        medicineInfoDataSource: MedicineInfoDataSource,
//        memoDataSource: MemoDataSource
//    ): MemoRepository {
//        return MemoRepositoryImpl(
//            memoDataSource = memoDataSource,
//            medicineGroupDataSource = remoteMedicineGroupDataSource,
//            medicineInfoDataSource = medicineInfoDataSource
//        )
//    }
}