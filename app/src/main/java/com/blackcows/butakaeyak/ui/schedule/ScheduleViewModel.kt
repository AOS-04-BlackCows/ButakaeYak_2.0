package com.blackcows.butakaeyak.ui.schedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Time
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository,
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Init)
    val uiState = _uiState.asStateFlow()

    private val _dateToMedicineGroup = MutableLiveData<List<MedicineGroup>>(listOf())
    val dateToMedicineGroup = _dateToMedicineGroup

    private val _scheduleProfile = MutableLiveData<List<ScheduleProfile>>(listOf())
    val scheduleProfile = _scheduleProfile

    fun getDateToMedicineGroup(userId: String, date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading

            val allGroups = medicineGroupRepository.getMyGroups(userId)
            Log.d("ScheduleViewModel", "allGroup size: ${allGroups.size}")
            _dateToMedicineGroup.value =  allGroups.filter {
                it.startedAt <= date && it.finishedAt >= date
            }
            //TODO: Test용 Mock Data
            //_dateToMedicineGroup.value = ScheduleMockData.medicineGroups

            _uiState.value = ScheduleUiState.Success
        }
    }

    fun changeToTimeToGroup(): List<TimeToGroup> {
        val alarmMap = mutableMapOf<String, MutableList<MedicineGroup>>()
        val groups = dateToMedicineGroup.value!!

        groups.forEach {
            it.alarms.forEach { alarm ->
                alarmMap.getOrPut(alarm) { mutableListOf() }.add(it)
            }
        }

        val notSortedList = alarmMap.map {
            val sortedByName = it.value.sortedBy { it.name }
            TimeToGroup(
                alarm = it.key, groups = sortedByName
            )
        }

        return notSortedList.sortedBy { it.alarm }
    }

    fun removeMedicineGroup(medicineGroup: MedicineGroup) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            medicineGroupRepository.removeGroup(medicineGroup)
            _dateToMedicineGroup.value = _dateToMedicineGroup.value!!.toMutableList().filter {
                it.id != medicineGroup.id
            }
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun checkTakenMedicineGroup(medicineGroup: MedicineGroup, taken: Boolean, alarm: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            medicineGroupRepository.notifyTaken(medicineGroup, taken, alarm)
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun getFriendProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading

            _scheduleProfile.value = friendRepository.getMyFriends(userId).map {
                val friendId = if(userId != it.proposer) it.proposer
                                else it.receiver
                userRepository.getProfileAndName(friendId)
            }

            _uiState.value = ScheduleUiState.Success
        }
    }

    fun clearScheduleProfiles() {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            _scheduleProfile.value = listOf()
            Log.d("ScheduleFragment", "viewModel: size is ${_scheduleProfile.value!!.size}")
            _uiState.value = ScheduleUiState.Success
        }
    }


}