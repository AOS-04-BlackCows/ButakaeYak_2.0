package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalMyPharmacyDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "PHARMACY_DATASOURCE"
        private const val PHARMACY_SHARED_PREFS = "PHARMACYS"
        private const val FAVORITE_PHARMACY = "PHARMACY_IN_INTEREST"
    }

    private val sharedPreferencesPharmacy: SharedPreferences = context.getSharedPreferences(
        PHARMACY_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editorPharmacy = sharedPreferencesPharmacy.edit()


    // Pharmacy
    fun getMyPharmacy(): List<KakaoPlacePharmacy> {
        val list = sharedPreferencesPharmacy.getString(FAVORITE_PHARMACY, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<KakaoPlacePharmacy>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<KakaoPlacePharmacy>()
        Log.d(TAG, "list Size: ${list.size}")

        return list
    }
    fun saveMyPharmacy(myPharmacy: List<KakaoPlacePharmacy>) {
        val gson = Gson()
        val json = gson.toJson(myPharmacy)
        editorPharmacy.putString(FAVORITE_PHARMACY, json).apply()
        Log.d(TAG, "saveMyPharmacy() Run. myPharmacy.size: ${myPharmacy.size}")
    }

    fun addMyPharmacy(pharmacy: KakaoPlacePharmacy) {
        saveMyPharmacy(
            listOf(pharmacy) + getMyPharmacy()
        )
        Log.d(TAG, "addMyPharmacy() Run. id: ${pharmacy.id}")
    }

    fun isPharmacyChecked(id: String) : Boolean {
        Log.d(TAG, "isPharmacyChecked() Run. id: ${id}")
        return getMyPharmacy().any {
            it.id == id
        }
    }

    fun removeMyPharmacy(id: String) {
        val lists = getMyPharmacy().toMutableList().filterNot {
            it.id == id
        }
        saveMyPharmacy(lists)
        Log.d(TAG, "removeMyPharmacy() Run. id: ${id}")
    }
}