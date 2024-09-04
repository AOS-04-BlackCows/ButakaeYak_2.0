package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.take.GetMyMedicinesUseCase
import com.blackcows.butakaeyak.domain.take.GetTodayMedicineUseCase
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyTakeViewModel @Inject constructor(
    private val getTodayMedicineUseCase: GetTodayMedicineUseCase,
    private val getMyMedicinesUseCase: GetMyMedicinesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<TakeUiState>(TakeUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun loadTodayMedicines(dayWeekDay: WeekDay) {
//        getTodayMedicineUseCase.invoke(dayWeekDay) { medicineAtTimes ->
//            if(medicineAtTimes.isNotEmpty()) {
//                _uiState.value = TakeUiState.GetTodayMedicinesSuccess(medicineAtTimes)
//            } else {
//                _uiState.value = TakeUiState.Failure
//            }
//        }
        _uiState.value = TakeUiState.GetTodayMedicinesSuccess(
            listOf(
                MedicineAtTime(dayWeekDay, "8:00", listOf(Medicine(id = "1", name = "기본약"), Medicine(id = "2",name = "비타민"), Medicine(id = "3",name = "영양제"))),
                MedicineAtTime(dayWeekDay, "12:00", listOf(Medicine(id = "1",name = "기본약"))),
                MedicineAtTime(dayWeekDay, "18:00", listOf(Medicine(id = "1",name = "기본약")))
            )
        )
    }

    fun loadMyMedicines() {
//        getMyMedicinesUseCase.invoke { myMedicines ->
//            if(myMedicines.isNotEmpty()) {
//                _uiState.value = TakeUiState.GetMyMedicinesSuccess(myMedicines)
//            } else {
//                _uiState.value = TakeUiState.Failure
//            }
//        }
        _uiState.value = TakeUiState.GetMyMedicinesSuccess(
            listOf(
                MyMedicine(Medicine(id = "1",name = "기본약"), mapOf(WeekDay.WEDNESDAY to listOf("8:00", "12:00", "18:00"))),
                MyMedicine(Medicine(id = "2",name = "비타민"), mapOf(WeekDay.WEDNESDAY to listOf("8:00"))),
                MyMedicine(Medicine(id = "3",name = "영양제"), mapOf(WeekDay.WEDNESDAY to listOf("8:00")))
            )
        )
    }
}