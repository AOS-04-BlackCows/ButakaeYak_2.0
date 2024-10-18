package com.blackcows.butakaeyak.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMessagingDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "FirebaseMessagingDataSource"
        private const val URL = "https://fcm.googleapis.com/v1/projects/yaktong-6651a/messages:send"
    }

    private val db = Firebase.firestore

//    suspend fun getOpenAiKey(): String {
//
//        return db.collection(KEY_COLLECTION)
//            .document(OPEN_AI_KEY).get()
//            .await()[CONTENT] as String
//    }
}