package com.blackcows.butakaeyak.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.ui.schedule.TimeToGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository
): ViewModel() {
    private val _myMedicineGroup = MutableLiveData(listOf<MedicineGroup>())
    val myMedicineGroup get() = _myMedicineGroup

    // 나의 모든 약그룹 가져오기
    fun getAllMedicineGroups (user: String) {
        viewModelScope.launch {
            _myMedicineGroup.value = medicineGroupRepository.getMyGroups(user)
        }
    }

    fun getGroupsOnDay(date: LocalDate): List<MedicineGroup> {
        return _myMedicineGroup.value!!.filter {
            it.startedAt <= date && it.finishedAt >= date
        }
    }

    // 오늘 복용할 약 불러오기
    fun getTodayMedicineGroups (): List<MedicineGroup> {
        val date = LocalDate.now()
        return _myMedicineGroup.value!!.filter {
            it.startedAt <= date && it.finishedAt >= date
        }
    }

    fun saveGroup (newGroup : MedicineGroupRequest) {
        viewModelScope.launch {
            medicineGroupRepository.saveNewGroup(newGroup)
            getAllMedicineGroups(newGroup.userId)
        }
    }

    fun checkTakenMedicineGroup(groupId: String, taken: Boolean, consumeFormat: String) {
        viewModelScope.launch {
            _myMedicineGroup.value = _myMedicineGroup.value!!.toMutableList().map {
                if(it.id == groupId) {
                    val hasTaken = it.hasTaken.toMutableList()
                    if(taken) {
                        hasTaken.add(consumeFormat)
                    } else hasTaken.remove(consumeFormat)

                    it.copy(
                        hasTaken = hasTaken
                    )
                } else it
            }

            medicineGroupRepository.notifyTaken(groupId, taken, consumeFormat)
        }
    }
    fun checkTakenMedicineGroup(medicineGroup: MedicineGroup, taken: Boolean, consumeFormat: String) {
        checkTakenMedicineGroup(medicineGroup.id, taken, consumeFormat)
    }


    fun removeMedicineGroup(medicineGroup: MedicineGroup) {
        viewModelScope.launch {
            _myMedicineGroup.value = _myMedicineGroup.value!!.toMutableList().filter {
                it.id != medicineGroup.id
            }

            medicineGroupRepository.removeGroup(medicineGroup)
        }
    }

    fun changeToTimeToGroup(list: List<MedicineGroup>): List<TimeToGroup> {
        val alarmMap = mutableMapOf<String, MutableList<MedicineGroup>>()

        list.forEach {
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