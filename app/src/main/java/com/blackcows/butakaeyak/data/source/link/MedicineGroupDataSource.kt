package com.blackcows.butakaeyak.data.source.link

import com.blackcows.butakaeyak.data.models.MedicineGroup

interface MedicineGroupDataSource {
    suspend fun getMedicineGroups(userId: String): List<MedicineGroup>
    suspend fun saveMedicineGroup(groups: List<MedicineGroup>)
    suspend fun addSingleGroup(group: MedicineGroup)
    suspend fun removeGroup(group: MedicineGroup)
}