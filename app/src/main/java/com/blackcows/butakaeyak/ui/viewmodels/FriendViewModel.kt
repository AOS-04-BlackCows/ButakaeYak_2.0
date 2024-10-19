package com.blackcows.butakaeyak.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.schedule.ScheduleUiState
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import com.google.firebase.functions.FirebaseFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val localUtilsRepository: LocalUtilsRepository
): ViewModel() {

    private val _friendProfiles = MutableLiveData<List<ScheduleProfile>>(listOf())
    val friendProfiles = _friendProfiles

    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Init)
    val uiState = _uiState.asStateFlow()


    fun getAllFriendProfiles(userId: String) {
        viewModelScope.launch {
            _friendProfiles.value = friendRepository.getMyFriends(userId).map {
                val friendId = if(userId != it.proposer) it.proposer
                else it.receiver
                userRepository.getProfileAndName(friendId)
            }
        }
    }

    fun clearScheduleProfiles() {
        viewModelScope.launch {
            _friendProfiles.value = listOf()
            Log.d("ScheduleFragment", "viewModel: size is ${_friendProfiles.value!!.size}")
        }
    }

    fun knockToFriend(senderId: String, friendId: String,deviceToken: String): Boolean {
        val limitTime = 60 * 1000       //60초
        val lastKnockTime = localUtilsRepository.getKnockHistory().getOrDefault(friendId, 0L)

        if(System.currentTimeMillis() - lastKnockTime < limitTime) return false

        viewModelScope.launch {
            val data = hashMapOf(
                "from" to senderId,
                "token" to deviceToken,
            )

            val result=  FirebaseFunctions.getInstance()
                .getHttpsCallable("test3")
                .call(data)
                .await()

            Log.d("FirebaseFunctions",result.data.toString())
        }

        return true
    }
}