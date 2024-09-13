package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.data.source.LocalDataSource.Companion
import com.blackcows.butakaeyak.data.source.LocalDataSource.Companion.USER_DATA
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalUtilsDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "LocalUtilsDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"
        const val USER_DATA = "USER_DATA"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveAutoLoginData(userData: UserData) {
        val gson = Gson()
        val json = gson.toJson(userData)
        editor.putString(USER_DATA, json).apply()
    }

    fun getSavedUserData(): UserData? {
        return sharedPreferences.getString(USER_DATA, null)?.let {
            val gson = Gson()
            gson.fromJson(it, UserData::class.java)
        }
    }

    fun deleteAutoLoginData() {
        editor.remove(USER_DATA).apply()
    }


}