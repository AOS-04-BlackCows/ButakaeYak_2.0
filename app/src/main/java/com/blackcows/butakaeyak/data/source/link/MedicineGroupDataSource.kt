package com.blackcows.butakaeyak.data.source.link

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest
import com.blackcows.butakaeyak.data.models.MedicineGroupResponse

interface MedicineGroupDataSource {
    suspend fun getMedicineGroupById(groupId: String): MedicineGroupResponse?
    suspend fun getMedicineGroups(userId: String): List<MedicineGroupResponse>

    suspend fun addSingleGroup(request: MedicineGroupRequest)
    suspend fun removeGroup(group: MedicineGroup)
    suspend fun updateGroup(takenGroup: MedicineGroup)
}