package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.AutoLoginData
import com.blackcows.butakaeyak.data.models.MedicineDetail

interface LocalUtilsRepository {
    fun hasLogin(): Boolean
    fun saveAutoLoginData(data: AutoLoginData)
    fun getAutoLoginData(): AutoLoginData?
    fun removeAutoLoginData()

    fun getKnockHistory(): Map<String, Long>
    fun saveKnockHistory(friendId: String, time: Long): Map<String, Long>
}