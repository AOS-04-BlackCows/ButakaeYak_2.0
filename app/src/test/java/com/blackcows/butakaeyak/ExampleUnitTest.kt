package com.blackcows.butakaeyak

import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.toMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

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
    fun toMapExposeTest() {
        val friends = Friend(
            id = "habemus", proposer = "indoctum", receiver = "similique", isConnected = false
        )

        val map = friends.toMap()
        println(map.toString())
    }

    @Test
    fun localDataParser() {
        val str = "2024-09-12"

        val toLocalDate = LocalDate.parse(str)

        println(toLocalDate)
    }

    @Test
    fun coroutineTest() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            println("World!")
        }
        println("Hello,")
    }
}