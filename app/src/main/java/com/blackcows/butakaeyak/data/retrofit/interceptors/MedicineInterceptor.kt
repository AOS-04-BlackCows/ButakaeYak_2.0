package com.blackcows.butakaeyak.data.retrofit.interceptors

import com.blackcows.butakaeyak.BuildConfig
import okhttp3.Interceptor

object MedicineInterceptor {
    fun get() =
        Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("x-algolia-application-id", BuildConfig.ALGORIA_APP_ID)
                .header("x-algolia-api-key", BuildConfig.ALGORIA_SEARCH_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
}