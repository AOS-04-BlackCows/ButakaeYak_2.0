package com.blackcows.butakaeyak.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ApiKeyDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "ApiKeyDataSource"
        private const val KEY_COLLECTION = "keys"

        private const val OPEN_AI_KEY = "OpenAi"


        private const val CONTENT = "Content"
    }

    private val db = Firebase.firestore

    suspend fun getOpenAiKey(): String {

        return db.collection(KEY_COLLECTION)
            .document(OPEN_AI_KEY).get()
            .await()[CONTENT] as String
    }
}