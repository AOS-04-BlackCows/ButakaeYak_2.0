package com.example.yactong.data.retrofit

import com.example.yactong.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val DRUG_REST_API_KEY = BuildConfig.DRUG_INFO_KEY

    private val retrofitInstances = mutableMapOf<ApiBaseUrl, Retrofit>()
    private val gson = GsonBuilder().setLenient().create()

    fun getInstance(baseUrl: ApiBaseUrl) : Retrofit {
        return retrofitInstances[baseUrl] ?: createRetrofitInstance(baseUrl).also {
            retrofitInstances[baseUrl] = it
        }
    }

    private fun createRetrofitInstance(baseUrl: ApiBaseUrl): Retrofit {
        val okHttpClient=  getOkhttpClient(baseUrl)

        return Retrofit.Builder()
            .baseUrl(baseUrl.url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    private fun getOkhttpClient(apiBaseUrl: ApiBaseUrl): OkHttpClient {
        return when(apiBaseUrl) {
            ApiBaseUrl.PillInfoUrl -> {
                OkHttpClient().newBuilder().build()
            }

            ApiBaseUrl.DrugInfoUrl -> {
                OkHttpClient().newBuilder().build()
            }
        }
    }

}