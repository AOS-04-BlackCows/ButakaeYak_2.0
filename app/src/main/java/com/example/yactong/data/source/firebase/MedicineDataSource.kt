package com.example.yactong.data.source.firebase

import android.util.Log
import com.algolia.search.dsl.searchableAttributes
import com.algolia.search.dsl.settings
import com.algolia.search.model.search.ResponseFields
import com.algolia.search.saas.Client
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.example.yactong.BuildConfig
import com.example.yactong.data.models.Medicine
import com.example.yactong.data.retrofit.DrugApiService
import com.example.yactong.data.toMap
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class MedicineDataSource @Inject constructor(
    private val retrofit: DrugApiService,
    private val algoliaClient: Client
) {
    companion object {
        private const val INDEX_NAME = "medicines"
        private const val TAG = "MedicineDataSource"
    }

    private val medicineIndex by lazy {
        algoliaClient.getIndex(INDEX_NAME)
    }

    suspend fun searchMedicinesByName(name: String): List<Medicine> {
        val result = mutableListOf<Medicine>()
        val settingsJson = JSONObject()
        settingsJson.put("searchableAttributes", JSONArray(listOf("name")))

        //TODO: 이게 맞나? 확인해야함.
        medicineIndex.setSettings(settingsJson)

        val jsonArray = medicineIndex.searchSync(Query(name))

        jsonArray?.getJSONArray("hits")?.let { medicines ->
            Log.d(TAG, "hits size: ${medicines.length()}")
            val gson = Gson()

            val seq = (0 until medicines.length()).asSequence().map { medicines.getJSONObject(it) }
            for(json in seq) {
                val data = gson.fromJson(json.toString(), Medicine::class.java)
                result.add(getImageUrl(data))
            }
        }

        return result
    }

    private suspend fun getImageUrl(medicine: Medicine): Medicine {
        val item = retrofit.getDrugInfo("").body.items

        return if(item.isNotEmpty())  medicine.copy(
            imageUrl = item[0].itemImage
        ) else medicine
    }

}