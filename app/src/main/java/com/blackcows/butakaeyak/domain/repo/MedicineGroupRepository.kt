package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest

interface MedicineGroupRepository {
    suspend fun getMyGroups(userId: String): List<MedicineGroup>

    suspend fun saveNewGroup(medicineGroupRequest: MedicineGroupRequest)
    suspend fun removeGroup(medicineGroup: MedicineGroup)

    suspend fun notifyTaken(groupId: String, taken: Boolean, takenFormat: String): MedicineGroup?
}