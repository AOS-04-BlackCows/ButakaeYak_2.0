package com.blackcows.butakaeyak.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.schedule.ScheduleUiState
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
): ViewModel() {

    private val _friendProfiles = MutableLiveData<List<ScheduleProfile>>(listOf())
    val friendProfiles = _friendProfiles

    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun getFriendProfile(userId: String) {
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
}