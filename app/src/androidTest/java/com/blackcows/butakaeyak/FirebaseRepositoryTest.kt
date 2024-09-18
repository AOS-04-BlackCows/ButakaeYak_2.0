package com.blackcows.butakaeyak

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.domain.repo.DrugRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.SignUpResult
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
//@Config(application = HiltTestApplication::class)
class FirebaseRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        println("--------------------------------------------------------------------------------------")
    }

    @After
    fun after() {
        println("--------------------------------------------------------------------------------------")
    }



    @Test
    fun userTest() = runBlocking {
        val userRequest = UserRequest(
            name = "Jeffrey Meadows",
            loginId = "Jeffrey",
            pwd = "Jeffrey123",
            profileUrl = null,
            kakaoId = null,
            deviceToken = null
        )
        val result = userRepository.signUpUserData(userRequest)

        when(result) {
            is SignUpResult.Success -> {
                println("Success")
            }
            is SignUpResult.KakaoSignUpFail -> {
                println("KakaoSignUpFail")
            }
            is SignUpResult.LoginIdDuplicate -> {
                println("LoginIdDuplicate")
            }
            is SignUpResult.Failure -> {
                println("Failure")
            }
        }
    }
}