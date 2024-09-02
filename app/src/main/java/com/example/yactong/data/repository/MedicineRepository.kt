package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Medicine
import com.example.yactong.data.models.Pill

interface MedicineRepository {
    fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit)
}