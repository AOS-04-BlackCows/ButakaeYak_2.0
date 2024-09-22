package com.blackcows.butakaeyak.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.ui.example.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository,
    private val friendRepository: FriendRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Init)
    val uiState = _uiState.asStateFlow()

    private val _medicineGroup = MutableLiveData<List<MedicineGroup>>(listOf())
    val medicineGroup = _medicineGroup

    private val _friends = MutableLiveData<List<Friend>>(listOf())
    val friends = _friends

    fun getMedicineGroup(userId: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            _medicineGroup.value = medicineGroupRepository.getMyGroups(userId)
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun getFriends(userId: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            _friends.value = friendRepository.getMyFriends(userId)
            _uiState.value = ScheduleUiState.Success
        }
    }


}