package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.blackcows.butakaeyak.data.models.AutoLoginData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalUtilsDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "LocalUtilsDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"

        private const val LOGIN_DATA = "LOGIN_DATA"
        private const val IS_LOGIN = "IS_LOGIN"

        private const val KNOCK_HISTORY = "KNOCK_HISTORY"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    private val encryptedPreferences by lazy {
        val masterKeyAlias = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


        EncryptedSharedPreferences.create(
            context,
            LOGIN_DATA,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isSignIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }
    fun setSignIn(isSignIn: Boolean) {
        editor.putBoolean(IS_LOGIN, isSignIn).apply()
    }

    fun saveAutoLoginData(loginData: AutoLoginData) {
        val gson = Gson()
        val json = gson.toJson(loginData)

        encryptedPreferences.edit().putString(LOGIN_DATA, json).apply()
    }

    fun getAutoLoginData(): AutoLoginData? {
        return encryptedPreferences.getString(LOGIN_DATA, null)?.let {
            val gson = Gson()
            gson.fromJson(it, AutoLoginData::class.java)
        }
    }

    fun deleteAutoLoginData() {
        encryptedPreferences.edit().remove(LOGIN_DATA).apply()
    }

    fun getKnockHistory(): Map<String, Long> {
        val json = sharedPreferences.getString(KNOCK_HISTORY, null)
        return if(json == null) mutableMapOf()
        else {
            val type= object : TypeToken<Map<String?, Long?>?>() {}.getType()
            Gson().fromJson(json, type)
        }
    }

    fun saveKnockHistory(friendId: String, time: Long): Map<String, Long> {
        val histories = getKnockHistory().toMutableMap()
        histories[friendId] = time

        val gson = Gson()
        val json = gson.toJson(histories)

        sharedPreferences.edit().putString(KNOCK_HISTORY, json).apply()
        return histories
    }


}