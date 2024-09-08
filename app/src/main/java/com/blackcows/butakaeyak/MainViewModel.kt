package com.blackcows.butakaeyak

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.PharmacyRepository
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val pharmacyRepository: PharmacyRepository
): ViewModel() {
    private val _pharmacies = MutableLiveData<Set<KakaoPlacePharmacy>>()
    val pharmacies get() = _pharmacies

    private val _myMedicine = MutableLiveData<Set<MyMedicine>>()
    val myMedicines get() = _myMedicine

    fun getPharmacyList(){
        val getPharmacy = pharmacyRepository.getMyPharmacy()
        _pharmacies.value = getPharmacy.toSet()
    }
    fun savePharmacyList(pharmacyList: List<KakaoPlacePharmacy>){
        pharmacyRepository.saveMyPharmacy(pharmacyList)
        _pharmacies.value = pharmacyList.toSet()
    }
    fun addToFavoritePharmacyList(pharmacy: KakaoPlacePharmacy){
        pharmacyRepository.addMyPharmacy(pharmacy)
        getPharmacyList()
    }
    fun cancelFavoritePharmacy(id: String) {
        if(pharmacies.value == null) {
            throw Exception("Please call getPharmacyList() to initialize LiveData.")
        }

        val removedList = pharmacies.value!!.filter {
            it.id != id
        }
        savePharmacyList(removedList)
    }
    fun isPharmacyChecked(id: String) : Boolean{
       return pharmacyRepository.isPharmacyChecked(id)
    }


    fun getMyMedicineList() {
        _myMedicine.value = localRepository.getMyMedicines().toSet()
    }
    fun saveMyMedicineList(myMedicines: List<MyMedicine>) {
        localRepository.saveMyMedicines(myMedicines)
        _myMedicine.value = myMedicines.toSet()
    }
    fun addToMyMedicineList(myMedicine: MyMedicine) {
        localRepository.addToMyMedicine(myMedicine)
        getMyMedicineList()
    }
    fun cancelMyMedicine(id: String) {
        if(myMedicines.value == null) {
            throw Exception("Please call getMyMedicineList() to initialize LiveData.")
        }

        val removedList = _myMedicine.value!!.filter {
            it.medicine.id != id
        }
        saveMyMedicineList(removedList)
    }
    fun isMyMedicine(id: String): Boolean {
        return localRepository.isMyMedicine(id)
    }

}
