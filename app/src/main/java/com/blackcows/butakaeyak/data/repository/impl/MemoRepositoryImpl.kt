package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.data.source.firebase.MedicineDataSource
import com.blackcows.butakaeyak.data.source.firebase.MemoDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import java.time.LocalDate
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource,
    private val medicineGroupDataSource: MedicineGroupDataSource
): MemoRepository {

    companion object {
        private const val TAG = "MemoRepositoryImpl"
    }

    override suspend fun getMemosFromWhen(userId: String, createdAt: LocalDate): List<Memo> {
        return runCatching {
            memoDataSource.getMemosFromWhen(userId, createdAt).map {
                Memo(
                    id = it.id,
                    userId = it.userId,
                    groupId = it.groupId,
                    content = it.content,
                    createdAt = LocalDate.parse(it.createdAt),
                    updatedAt = LocalDate.parse(it.updatedAt)
                )
            }
        }.getOrDefault(listOf())
    }

    override suspend fun createMemo(memo: Memo) {
        runCatching {
            memoDataSource.saveMemo(memo)
        }.onFailure {
            Log.w(TAG, "createMemo Failed) msg: ${it.message}")
        }
    }

    override suspend fun editMemo(memo: Memo) {
        runCatching {
            val updated = memo.copy(
                updatedAt = LocalDate.now()
            )
            memoDataSource.editMemo(updated)
        }.onFailure {
            Log.w(TAG, "editMemo Failed) msg: ${it.message}")
        }
    }

    override suspend fun deleteMemo(memo: Memo) {
        runCatching {
            memoDataSource.deleteMemo(memo)
        }.onFailure {
            Log.w(TAG, "deleteMemo Failed) msg: ${it.message}")
        }
    }

}