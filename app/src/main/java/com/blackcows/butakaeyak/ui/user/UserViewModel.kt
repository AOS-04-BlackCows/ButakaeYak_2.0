package com.blackcows.butakaeyak.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData

class UserViewModel : ViewModel() {

    // 현재 사용자 등록
    private val _currentUser = MutableLiveData<UserData>()
    val currentUser : LiveData<UserData> = _currentUser


    fun setCurrentUser(userData : UserData) {
        _currentUser.value = userData
    }
}