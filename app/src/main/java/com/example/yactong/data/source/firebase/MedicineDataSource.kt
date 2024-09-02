package com.example.yactong.data.source.firebase

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
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class MedicineDataSource @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val retrofit: DrugApiService,
    private val algoliaClient: Client
) {
    companion object {
        private const val INDEX_NAME = "medicines"
    }

    private val medicineIndex by lazy {
        algoliaClient.getIndex(INDEX_NAME)
    }

    suspend fun searchMedicinesByName(name: String) = suspendCancellableCoroutine<List<Medicine>> { continuation ->
        val result = mutableListOf<Medicine>()
        val setting = settings {
            searchableAttributes { +"name" }
        }

        //TODO: 이게 맞나? 확인해야함.
        medicineIndex.setSettings(JSONObject(setting.toMap()))

        //TODO: Query 확인해야함. or and 필요
        medicineIndex.searchAsync(Query(name)) { jsonArray, exception ->
            if (exception != null) {
                continuation.resumeWithException(exception)
            } else {
                jsonArray?.getJSONArray(INDEX_NAME)?.let { medicines ->
                    CoroutineScope(dispatcher).launch {
                        val gson = Gson()

                        val seq = (0 until medicines.length()).asSequence().map { medicines.getJSONObject(it) }
                        for(json in seq) {
                            val data = gson.fromJson(json.toString(), Medicine::class.java)
                            result.add(getImageUrl(data))
                        }
                    }
                } ?: continuation.resumeWithException(RuntimeException("No Medicine found."))
            }
        }

        continuation.resume(result)
    }

    private suspend fun getImageUrl(medicine: Medicine): Medicine {
        val item = retrofit.getDrugInfo("").body.items

        return if(item.isNotEmpty())  medicine.copy(
            imageUrl = item[0].itemImage
        ) else medicine
    }

}