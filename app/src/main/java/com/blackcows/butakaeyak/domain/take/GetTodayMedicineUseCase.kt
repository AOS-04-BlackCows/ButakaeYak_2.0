package com.blackcows.butakaeyak.domain.take

import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import io.ktor.util.date.WeekDay
import javax.inject.Inject

class GetTodayMedicineUseCase @Inject constructor(
    //private val localRepository: LocalRepository
){
    operator fun invoke(
        weekDay: WeekDay,
        callback: (List<MedicineAtTime>) -> (Unit)) {
        //localRepository.getTodayMedicines(weekDay, callback)
        callback(listOf(MedicineAtTime("18:00", listOf())))
    }
}