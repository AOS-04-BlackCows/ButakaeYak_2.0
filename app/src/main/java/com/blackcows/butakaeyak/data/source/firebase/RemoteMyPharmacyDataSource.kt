package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.data.source.link.MyPharmacyDataSource
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteMyPharmacyDataSource @Inject constructor(

): MyPharmacyDataSource {

    companion object {
        private const val TAG = "RemoteMyPharmacyDataSource"
        private const val MY_PHARMACY_COLLECTION = "myPharmacies"

        private const val PHARMACY_ID = "id"
        private const val USER_ID = "userId"
    }
    private val db = Firebase.firestore


    override suspend fun getMyPharmacies(userId: String): List<MyPharmacy> {
        return kotlin.runCatching {
            db.collection(MY_PHARMACY_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .get().await().toObjects(MyPharmacy::class.java)
        }.getOrDefault(listOf())
    }

    override suspend fun saveMyPharmacies(pharmacies: List<MyPharmacy>) {
        coroutineScope {
            kotlin.runCatching {
                val tasks = pharmacies.map {
                    async {
                        db.collection(MY_PHARMACY_COLLECTION)
                            .add(it.toMap())
                            .await()
                    }
                }
                tasks.awaitAll()
            }.onFailure {
                Log.w(TAG, "SaveMyPharmacies is Failed due to ${it.message}")
            }
        }
    }

    override suspend fun addSinglePharmacy(userId: String, pharmacy: MyPharmacy) {
        val myList = getMyPharmacies(userId)
        saveMyPharmacies(myList + pharmacy)
    }

    override suspend fun removePharmacy(pharmacy: MyPharmacy) {
        kotlin.runCatching {
            val obj = db.collection(MY_PHARMACY_COLLECTION)
                .whereEqualTo(PHARMACY_ID, pharmacy.id)
                .get().await()

            if(obj.documents.isNotEmpty()) {
                val id = obj.documents[0].id

                db.collection(MY_PHARMACY_COLLECTION)
                    .document(id)
                    .delete()
            }
        }.onFailure {
            Log.w(TAG, "removePharmacy is Failed due to ${it.message}")
        }
    }
}