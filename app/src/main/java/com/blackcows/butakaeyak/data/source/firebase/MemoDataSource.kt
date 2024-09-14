package com.blackcows.butakaeyak.data.source.firebase

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.source.firebase.UserDataSource.Companion.DUPLICATED_EXCEPTION
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MemoDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "UserSource"
        private const val MEMO_COLLECTION = "memos"

        private const val USER_ID = "userId"
        private const val MEDICINE_GROUP_ID = "groupId"
    }


    private val db = Firebase.firestore

    suspend fun saveMemo(memo: Memo) {
        db.collection(MEMO_COLLECTION)
            .add(memo.toMap()).await()
    }
    suspend fun getMemoById(id: String): Memo? {
        val memo = db.collection(MEMO_COLLECTION)
                        .document(id)
                        .get().await()?

        return memo
    }
}