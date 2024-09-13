package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.models.MedicineGroup
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


    override fun getMedicineGroups(userId: String): List<MedicineGroup> {
        val list = sharedPreferences.getString(MEDICINE_DETAIL, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MedicineGroup>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<MedicineGroup>()

        return list
    }

    override fun saveMedicineGroup(groups: List<MedicineGroup>) {
        val gson = Gson()
        val json = gson.toJson(groups)
        editor.putString(MEDICINE_DETAIL, json).apply()
    }

    override fun addSingleGroup(group: MedicineGroup) {
        saveMedicineGroup(
            listOf(group) + getMedicineGroups("0")
        )
    }

    override fun removeGroup(group: MedicineGroup) {
        val lists = getMedicineGroups("").toMutableList().filterNot {
            it == group
        }
        saveMedicineGroup(lists)
    }
}