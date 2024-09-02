package com.example.yactong.data.repository.impl

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Medicine
import com.example.yactong.data.models.Pill
import com.example.yactong.data.repository.MedicineRepository
import com.example.yactong.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
): MedicineRepository {

    override fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit) {

    }

}