package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Medicine
import com.example.yactong.data.models.Pill

interface MedicineRepository {
    fun addDrug(drug: Drug, callback: (Boolean) -> Unit)
    fun searchDrugs(name: String, callback: (List<Drug>) -> Unit)

    fun addPill(pill: Pill, callback: (Boolean) -> Unit)
    fun searchPills(name: String, callback: (List<Pill>) -> Unit)

    fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit)
}