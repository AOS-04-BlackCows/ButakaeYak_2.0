package com.example.yactong

import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.DrugApiService
import com.example.yactong.data.retrofit.RetrofitClient
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DataSourceUnitTest {
    @Test
    fun api() = runBlocking {
        val instance = RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl)
        val retrofit = instance.create(DrugApiService::class.java)

        val result = retrofit.getDrugInfo("한미아스피린")

        println("resultCode: ${result.header.resultCode}, item: ${result.body.items[0].itemName}")
    }

    @Test
    fun apiPill() = runBlocking {
        val instance = RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl)
        val retrofit = instance.create(DrugApiService::class.java)

        val result = retrofit.getPillInfo("타이레놀")

        println("resultCode: ${result.header.resultCode}, name: ${result.body.items[0].name}")
    }
}