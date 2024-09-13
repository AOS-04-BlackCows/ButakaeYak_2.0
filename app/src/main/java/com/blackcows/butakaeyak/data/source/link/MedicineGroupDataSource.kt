package com.blackcows.butakaeyak.data.source.link

import com.blackcows.butakaeyak.data.models.MedicineGroup

interface MedicineGroupDataSource {
    fun getMedicineGroups(userId: String): List<MedicineGroup>
    fun saveMedicineGroup(groups: List<MedicineGroup>)
    fun addSingleGroup(group: MedicineGroup)
    fun removeGroup(group: MedicineGroup)
}