package com.blackcows.butakaeyak.domain.take

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.repository.LocalRepository
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import io.ktor.util.date.WeekDay
import javax.inject.Inject

class GetMyMedicinesUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    operator fun invoke(
        callback: (List<MyMedicine>) -> Unit) {
        val result = localRepository.getMyMedicines()
        callback(result)
    }
}