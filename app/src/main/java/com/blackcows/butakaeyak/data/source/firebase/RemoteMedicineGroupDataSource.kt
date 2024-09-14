package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteMedicineGroupDataSource @Inject constructor(

): MedicineGroupDataSource {

    companion object {
        private const val TAG = "RemoteMedicineGroupDataSource"

        private const val MEDICINE_GROUP_COLLECTION = "medicineGroups"

        private const val USER_ID = "userId"
        private const val STARTED_AT = "startedAt"
        private const val FINISHED_AT = "finishedAt"
    }
    private val db = Firebase.firestore

    override suspend fun getMedicineGroups(userId: String): List<MedicineGroup>
        = kotlin.runCatching {
            db.collection(MEDICINE_GROUP_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .get().await().toObjects(MedicineGroup::class.java)
        }.getOrDefault(listOf())


    override suspend  fun saveMedicineGroup(groups: List<MedicineGroup>) {
        kotlin.runCatching {
            coroutineScope {
                val tasks = groups.map {
                    async {
                        db.collection(MEDICINE_GROUP_COLLECTION)
                            .add(groups)
                    }
                }

                tasks.awaitAll()
            }
        }.getOrDefault(listOf())
    }

    override suspend fun addSingleGroup(group: MedicineGroup) {
        kotlin.runCatching {
            db.collection(MEDICINE_GROUP_COLLECTION)
                .add(group.toMap()).await()
        }.onFailure {
            Log.w(TAG, "Adding single Medicine Group is failed due to ${it.message}")
        }
    }

    //TODO: id expose한거 처리하기!
    override suspend fun removeGroup(group: MedicineGroup) {
        TODO("Not yet implemented")
    }

}