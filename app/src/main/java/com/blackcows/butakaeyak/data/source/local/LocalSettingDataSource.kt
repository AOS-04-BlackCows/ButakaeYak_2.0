package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource.Companion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalSettingDataSource @Inject constructor(
    @ApplicationContext context: Context
){
    companion object {
        private const val TAG = "LocalSettingDataSource"

        private const val SETTING_PREFS = "BUAKAEYAK_SETTING"

        private const val ALARMS = "ALARMS"
        private const val IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SETTING_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true)
    }
    fun setIsFirstFalse() {
        editor.putBoolean(IS_FIRST_LAUNCH, false).apply()
    }

    fun saveAlarms(alarms: List<String>) {
        val gson = Gson()
        val jsonString = gson.toJson(alarms)

        editor.putString(ALARMS, jsonString).apply()
    }

    fun getAlarms(): List<String> {
        val json = kotlin.runCatching {
            sharedPreferences.getString(ALARMS, "")
        }.getOrElse {
            return listOf()
        }

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type

        return gson.fromJson(json, type) ?: listOf()
    }
}