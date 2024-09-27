package com.blackcows.butakaeyak

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.LocalSettingRepository
import com.blackcows.butakaeyak.domain.repo.PharmacyRepository
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val pharmacyRepository: PharmacyRepository,
    private val localSettingRepository: LocalSettingRepository
): ViewModel() {
    private val _pharmacies = MutableLiveData<Set<KakaoPlacePharmacy>>(setOf())
    val pharmacies get() = _pharmacies

    private val _myMedicine = MutableLiveData<Set<MyMedicine>>(setOf())
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


    fun getMyMedicineOnList(id: String): MyMedicine? {
        return localRepository.getMyMedicines().toSet().find {
            it.medicine.id == id
        }
    }
    fun getMyMedicineList() {
        _myMedicine.value = localRepository.getMyMedicines().toSet()
    }
    fun saveMyMedicineList(myMedicines: List<MyMedicine>) {
        localRepository.saveMyMedicines(myMedicines)
        _myMedicine.value = myMedicines.toSet()
    }
    fun addToMyMedicineList(myMedicine: MyMedicine) {
        cancelMyMedicine(myMedicine.medicine.id!!)

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


    fun isFirstLaunch(): Boolean {
        return localSettingRepository.isFirstLaunch()
    }
    fun setFirstLaunchFalse() {
        localSettingRepository.setFirstLaunchFalse()
    }
    fun setDefaultAlarm() {
        localSettingRepository.saveDefaultAlarms(
            listOf("08:00", "13:00", "18:00")
        )
    }
}
