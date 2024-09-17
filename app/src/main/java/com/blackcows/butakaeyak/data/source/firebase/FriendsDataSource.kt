package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.FriendRequest
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectsWithId
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

    suspend fun propose(userId: String, opponentId: String) {
        runCatching {
            val obj = FriendRequest(
                proposer = userId,
                receiver = opponentId,
                isConnected = false
            )
            db.collection(FRIEND_COLLECTION)
                .add(obj)
                .await()
        }.onFailure {
            Log.w(TAG, "propose failed) msg: ${it.message}")
        }
    }

    suspend fun getMyProposes(userId: String): List<Friend>
        = kotlin.runCatching {
            db.collection(FRIEND_COLLECTION)
                .whereEqualTo(PROPOSER, userId)
                .get().await().toObjectsWithId<Friend>()
            }.getOrDefault(listOf())

    suspend fun getMyReceives(userId: String): List<Friend>
        = kotlin.runCatching {
            db.collection(FRIEND_COLLECTION)
                .whereEqualTo(RECEIVER, userId)
                .get().await().toObjectsWithId<Friend>()
        }.getOrDefault(listOf())

    suspend fun getMyFriends(userId: String): List<Friend> = coroutineScope {
        val propose =  async { getMyProposes(userId) }
        val receive = async { getMyReceives(userId) }

        (receive.await() + propose.await()).filter {
            it.isConnected
        }
    }

    suspend fun cancelFriend(userId: String, opponentId: String) {
        kotlin.runCatching {
            val relationship = getMyFriends(userId).firstOrNull {
                it.isConnected && (it.proposer == opponentId || it.receiver == opponentId)
            }

            if(relationship != null) {
                db.collection(FRIEND_COLLECTION)
                    .document(relationship.id)
                    .delete()
                    .await()
            } else {
                Log.w(TAG, "friends에 등록돼있지 않은데 삭제 시도함")
            }
        }.onFailure {
            Log.w(TAG, "cancelFriend failed) msg: ${it.message}")
        }
    }
}