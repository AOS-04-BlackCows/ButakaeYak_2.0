package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest
import com.blackcows.butakaeyak.data.models.MedicineGroupResponse
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.LocalSettingRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.take.data.CycleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeAddViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository,
    private val localSettingRepository: LocalSettingRepository
) : ViewModel() {
    private val _medicineGroup = MutableLiveData<MedicineGroup?>(null)
    val medicineGroup get() = _medicineGroup
    private val medicineNameList = mutableListOf<String>()


    //TODO 변수 생성
    var groupName : String? = null
    var userId: String? = null
    var medicineIdList: List<String>? = null
    var customNameList: List<String>? = null
    var imageUrlList: List<String>? = null
    var startDate : String? = null
    var finishDate : String? = null
    var daysOfWeeks : List<String>? = null
    var alarms : List<String>? = null
    var hasTaken: List<String>? = null

//    TODO createNewMedicineGroupRequest
    fun createNewMedicineGroupRequest(onFailed:() -> (Unit)):MedicineGroupRequest?{
        if(groupName == null || startDate == null || finishDate == null
            || alarms == null || customNameList == null || imageUrlList == null){
            onFailed()
            return null
        } else {
            return MedicineGroupRequest(
                name = groupName!!, userId = userId!!, medicineIdList = medicineIdList!!,
                customNameList = customNameList!!, imageUrlList = imageUrlList!!,
                daysOfWeeks = daysOfWeeks!!, startedAt = startDate!!,
                finishedAt = finishDate!!, alarms = alarms!!, hasTaken = hasTaken!!)
        }
    }

    fun saveGroup (newGroup : MedicineGroupRequest) {
        viewModelScope.launch {
            medicineGroupRepository.saveNewGroup(newGroup)
        }
    }

    fun saveNames(medicines: MutableList<String>){
        medicineNameList.addAll(medicines)
    }
    fun loadNames(): List<TakeAddMedicine> {
        return medicineNameList.map {
            TakeAddMedicine(
                imageUrl = "medicine_type_1",
                name = it,
                isDetail = false
            )
        }
    }
    fun getDefaultAlarms(): List<String> {
        return localSettingRepository.getDefaultAlarms()
    }
}