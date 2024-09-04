package com.blackcows.butakaeyak.data.retrofit

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.retrofit.interceptors.MedicineInterceptor
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

object RetrofitClientXml {
    const val DRUG_REST_API_KEY = BuildConfig.DRUG_INFO_KEY

    private val retrofitInstances = mutableMapOf<ApiBaseUrl, Retrofit>()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(baseUrl: ApiBaseUrl) : Retrofit {
        return retrofitInstances[baseUrl] ?: createRetrofitInstance(baseUrl).also {
            retrofitInstances[baseUrl] = it
        }
    }

    private fun createRetrofitInstance(baseUrl: ApiBaseUrl): Retrofit {
        val okHttpClient = getOkhttpClient(baseUrl)

        return Retrofit.Builder()
            .baseUrl(baseUrl.url)
            .client(okHttpClient)
            .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
            .build()
    }



    private fun getOkhttpClient(apiBaseUrl: ApiBaseUrl): OkHttpClient {
        return when(apiBaseUrl) {
            ApiBaseUrl.DrugInfoUrl -> {
                OkHttpClient().newBuilder().build()
            }
            ApiBaseUrl.KakaoPlaceSearchUrl -> {
                OkHttpClient().newBuilder().addInterceptor(KakaoInterceptor.getInterceptor()).build()
            }
            ApiBaseUrl.MedicineUrl -> {
                OkHttpClient().newBuilder().addInterceptor(MedicineInterceptor.get()).build()
            }
            ApiBaseUrl.PharmacyListInfoUrl -> {
                OkHttpClient().newBuilder().build()
            }
        }
    }

}