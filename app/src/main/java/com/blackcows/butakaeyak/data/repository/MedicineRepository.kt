package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.Pill

interface MedicineRepository {
    fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit)
}