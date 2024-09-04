package com.blackcows.butakaeyak.ui.take

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.blackcows.butakaeyak.ui.take.data.MyMedicine

sealed class TakeUiState {
    data class GetTodayMedicinesSuccess(val medicineAtTimes: List<MedicineAtTime>): TakeUiState()
    data class GetMyMedicinesSuccess(val medicines: List<MyMedicine>): TakeUiState()

    data object Init: TakeUiState()
    data object Failure: TakeUiState()
}