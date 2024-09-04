package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import javax.inject.Inject

interface LocalRepository {
    fun getMyMedicines(): List<MyMedicine>
    fun saveMyMedicines(list: List<MyMedicine>)
    fun addToMyMedicine(myMedicine: MyMedicine)
}