package com.blackcows.butakaeyak.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.repository.impl.MedicineGroupRepositoryImpl
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.schedule.ScheduleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val medicineGroupRepository: MedicineGroupRepository
) : ViewModel() {
    // TODO 뷰모델 기능 정리
    private val _user = MutableLiveData(listOf<Medicine>())
    val user get() = _user

    private val _medicineGroup = MutableLiveData(listOf<MedicineGroup>())
    val medicineGroup get() = _medicineGroup
    // 오늘 복용할 약 불러오기
    fun getTodayMedicine (user: String) {
        viewModelScope.launch {
            //_medicineGroup.value = listOf()
            val date = LocalDate.now()
            _medicineGroup.value = medicineGroupRepository.getMyGroups(user).filter {
                it.startedAt <= date && it.finishedAt >= date
            }
        }
    }
    fun checkTakenMedicineGroup(userId: String, groupId: String, taken: Boolean, alarm: String) {
        viewModelScope.launch {
            MainNavigation.showLoadingBar()
            medicineGroupRepository.notifyTaken(groupId, taken, alarm)
            getTodayMedicine(userId)
            MainNavigation.disableLoadingBar()
        }
    }
    // 가족 데이터 불러오기
    fun getfamilyData () {

    }
    // 노크하기 클릭시 이벤트 적용
    fun sendKnockFamily () {

    }
}