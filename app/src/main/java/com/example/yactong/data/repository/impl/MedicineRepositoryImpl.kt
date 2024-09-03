package com.example.yactong.data.repository.impl

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Medicine
import com.example.yactong.data.models.Pill
import com.example.yactong.data.repository.MedicineRepository
import com.example.yactong.data.source.firebase.MedicineDataSource
import com.example.yactong.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val medicineDataSource: MedicineDataSource
): MedicineRepository {

    override fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit) {
        CoroutineScope(dispatcher).launch {
            val medicines = medicineDataSource.searchMedicinesByName(name)
            callback(medicines)
        }
    }

}