package com.blackcows.butakaeyak.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.schedule.ScheduleUiState
import com.blackcows.butakaeyak.ui.schedule.TimeToGroup
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MedicineGroupViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository
): ViewModel() {
    private val _dateToMedicineGroup = MutableLiveData<List<MedicineGroup>>(listOf())
    val dateToMedicineGroup = _dateToMedicineGroup

    fun getDateToMedicineGroup(userId: String, date: LocalDate) {
        viewModelScope.launch {
            val allGroups = medicineGroupRepository.getMyGroups(userId)
            Log.d("ScheduleViewModel", "allGroup size: ${allGroups.size}")
            _dateToMedicineGroup.value =  allGroups.filter {
                it.startedAt <= date && it.finishedAt >= date
            }
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
}