package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.Friend
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendsDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "FriendsDataSource"
        private const val FRIEND_COLLECTION = "friends"

        private const val PROPOSER = "proposer"
        private const val RECEIVER = "receiver"
        private const val IS_CONNECTED = "isConnected"
    }

    private val db = Firebase.firestore

    suspend fun getMyProposes(userId: String): List<Friend> {
        val result = db.collection(FRIEND_COLLECTION)
            .whereEqualTo(PROPOSER, userId)
            .get().await().toObjects(Friend::class.java)

        return result
    }

    suspend fun getMyReceives(userId: String): List<Friend> {
        val result = db.collection(FRIEND_COLLECTION)
            .whereEqualTo(RECEIVER, userId)
            .get().await().toObjects(Friend::class.java)

        return result
    }

    suspend fun getMyFriends(userId: String): List<Friend> = coroutineScope {
        val propose =  async { getMyProposes(userId) }
        val receive = async { getMyReceives(userId) }

        (receive.await() + propose.await()).filter {
            it.isConnected
        }
    }

    suspend fun cancelFriend(userId: String, opponentId: String) {
        val proposer = db.collection(FRIEND_COLLECTION)
            .whereEqualTo(PROPOSER, userId)
            .whereEqualTo(RECEIVER, opponentId)
            .get().await()

        if(!proposer.isEmpty) {
            val id = proposer.documents[0].id
            db.collection(FRIEND_COLLECTION)
                .document(id)
                .delete().await()
            return
        }

        val receiver = db.collection(FRIEND_COLLECTION)
            .whereEqualTo(PROPOSER, opponentId)
            .whereEqualTo(RECEIVER, userId)
            .get().await()

        if(!receiver.isEmpty) {
            val id = receiver.documents[0].id
            db.collection(FRIEND_COLLECTION)
                .document(id)
                .delete().await()
            return
        }

        Log.w(TAG, "friends에 등록돼있지 않은데 삭제 시도함")
    }
}