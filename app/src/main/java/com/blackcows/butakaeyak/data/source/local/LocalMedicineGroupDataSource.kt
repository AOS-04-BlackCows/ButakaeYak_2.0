package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest
import com.blackcows.butakaeyak.data.models.MedicineGroupResponse
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalMedicineGroupDataSource @Inject constructor(
    @ApplicationContext context: Context
): MedicineGroupDataSource {
    companion object {
        private const val TAG = "LocalUtilsDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"
        private const val MEDICINE_DETAIL = "MEDICINE_DETAIL"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    override suspend fun getMedicineGroupById(groupId: String): MedicineGroupResponse? {
        return getMedicineGroups("").firstOrNull {
            it.id == groupId
        }
    }

    override suspend fun getMedicineGroups(userId: String): List<MedicineGroupResponse> {
        val list = sharedPreferences.getString(MEDICINE_DETAIL, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MedicineGroupResponse>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<MedicineGroupResponse>()

        return list
    }

    private fun saveMedicineGroup(groups: List<MedicineGroupResponse>) {
        val gson = Gson()
        val json = gson.toJson(groups)
        editor.putString(MEDICINE_DETAIL, json).apply()
    }

    override suspend fun addSingleGroup(request: MedicineGroupRequest) {
        val list = getMedicineGroups("0").toMutableList()

        val randomId = getRandomStringId()
        list.add(request.toResponse(randomId))
        saveMedicineGroup(list)
    }

    override suspend fun removeGroup(group: MedicineGroup) {
        val lists = getMedicineGroups("").toMutableList().filterNot {
            it.id == group.id
        }
        saveMedicineGroup(lists)
    }

    override suspend fun updateGroup(takenGroup: MedicineGroup) {
        val list = getMedicineGroups("").toMutableList()
        val component = list.filter { it.id == takenGroup.id }

        if(component.isEmpty()) {
            throw Exception("존재하지 않는 Local MedicineGroup에 접근 시도함.")
        }

        val index = list.indexOf(component[0])

        list.removeAt(index)
        list.add(takenGroup.toResponse())

        saveMedicineGroup(list)
    }

    private fun getRandomStringId(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..20)
            .map { charset.random() }
            .joinToString("")
    }
}