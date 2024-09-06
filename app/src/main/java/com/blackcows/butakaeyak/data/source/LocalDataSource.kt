package com.blackcows.butakaeyak.data.source

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "LocalDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"
        const val MY_MEDICINES = "MEDICINES_IN_CONSUMING"
        const val FAVORITE_MEDICINES = "MEDICINES_IN_INTEREST"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun getMyMedicines(): List<MyMedicine>
        = sharedPreferences.getString(MY_MEDICINES, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<Array<MyMedicine>>() {}.type
            return gson.fromJson(it, type)
        } ?: listOf()

    fun saveMyMedicines(myMedicines: List<MyMedicine>) {
        val gson = Gson()
        val json = gson.toJson(myMedicines)
        editor.putString(MY_MEDICINES, json).apply()
    }

    fun addMyMedicines(myMedicine: MyMedicine) {
        saveMyMedicines(
            listOf(myMedicine) + getMyMedicines()
        )
    }

    fun isItemChecked(id: String) : Boolean {
        return getMyMedicines().any {
            it.medicine.id == id
        }
    }




    //TODO: ????
//    fun loadAllData(context: android.content.Context, fileName: String) : Medicine {
//        val pref = context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString()
//        Log.d(TAG,context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString())
//        return parseJson(pref)
//    }
//    fun parseJson(jsonString: String): Medicine {
//        val gson = Gson()
//        return gson.fromJson(jsonString, Medicine::class.java)
//    }
}