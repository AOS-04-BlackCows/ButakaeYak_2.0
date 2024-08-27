package com.example.yactong

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
        val currentTimeMs = System.currentTimeMillis()
        println("testRunBlockingTest")
        delay(5000)
        println("done testRunBlockingTest ${System.currentTimeMillis() - currentTimeMs} ms")
    }
}