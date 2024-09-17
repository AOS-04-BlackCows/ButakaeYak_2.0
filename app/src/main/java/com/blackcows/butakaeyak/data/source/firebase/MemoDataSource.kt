package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.data.models.MemoRequest
import com.blackcows.butakaeyak.data.models.MemoResponse
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.source.firebase.UserDataSource.Companion.DUPLICATED_EXCEPTION
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectWithId
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class MemoDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "UserSource"
        private const val MEMO_COLLECTION = "memos"

        private const val USER_ID = "userId"
        private const val MEDICINE_GROUP_ID = "groupId"
        private const val CREATED_AT = "createdAt"
    }

    private val db = Firebase.firestore

    suspend fun getMemosFromWhen(userId: String, createdAt: LocalDate): List<MemoResponse> {
        return runCatching {
            db.collection(MEMO_COLLECTION)
                .whereEqualTo(USER_ID, userId)
                .whereGreaterThanOrEqualTo(CREATED_AT, createdAt.toString())
                .get().await().toObjectsWithId<MemoResponse>()
        }.getOrDefault(listOf())
    }

    suspend fun saveMemo(memo: Memo) {
        kotlin.runCatching {
            val request = memo.toRequest()

            db.collection(MEMO_COLLECTION)
                .add(request.toMap())
                .await()
        }
    }

    suspend fun editMemo(memo: Memo) {
        kotlin.runCatching {
            val request = memo.toRequest()

            db.collection(MEMO_COLLECTION)
                .document(memo.id)
                .set(request.toMap())
                .await()
        }
    }

    suspend fun getMemoById(id: String): MemoResponse? =
        kotlin.runCatching {
            db.collection(MEMO_COLLECTION)
                .document(id)
                .get().await().toObjectWithId<MemoResponse>()
        }.getOrNull()

    suspend fun deleteMemo(memo: Memo) {
        runCatching {
            db.collection(MEMO_COLLECTION)
                .document(memo.id)
                .delete().await()
        }.onFailure {
            Log.w(TAG, "deleteMemo failed) msg: ${it.message}")
        }
    }


}