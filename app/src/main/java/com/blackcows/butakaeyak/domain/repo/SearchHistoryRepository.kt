package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.MedicineDetail

interface SearchHistoryRepository {
    fun getQueryHistory(): List<String>
    fun saveQueryHistory(query: String)
    fun removeQueryHistory()

    fun getMedicineDetailHistory(): List<String>
    fun saveMedicineDetailHistory(medicine: MedicineDetail)
    fun removeMedicineDetailHistory()
}