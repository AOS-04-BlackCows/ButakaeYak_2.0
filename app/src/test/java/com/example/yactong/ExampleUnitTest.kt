package com.example.yactong

import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.DrugApiService
import com.example.yactong.data.retrofit.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

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