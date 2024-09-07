package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.take.data.MyMedicine

interface LocalRepository {
    fun getMyMedicines(): List<MyMedicine>
    fun saveMyMedicines(list: List<MyMedicine>)
    fun addToMyMedicine(myMedicine: MyMedicine)

    fun isMyMedicine(id: String): Boolean
    fun cancelMyMedicine(id: String)

    fun saveUserData(userData: UserData)
    fun hasSavedUserData(): UserData?
}