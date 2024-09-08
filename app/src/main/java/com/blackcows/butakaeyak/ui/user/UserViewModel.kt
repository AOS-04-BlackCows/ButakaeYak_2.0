package com.blackcows.butakaeyak.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // 현재 사용자 등록
    private val _currentUser = MutableLiveData<UserData>()
    val currentUser get() = _currentUser


    fun loadUser() {
        localRepository.hasSavedUserData()?.let {
            currentUser.value = it
        }
    }
    fun setUser(userData: UserData) {
        currentUser.value = userData
    }
    suspend fun logout() {
        localRepository.deleteUserData()
        userRepository.logoutKakao()
        currentUser.value = null
    }
}