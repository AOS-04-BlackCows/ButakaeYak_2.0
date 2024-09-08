//package com.blackcows.butakaeyak
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
//import com.blackcows.butakaeyak.domain.repo.LocalRepository
//import com.blackcows.butakaeyak.domain.repo.PharmacyRepository
//import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
//import com.blackcows.butakaeyak.ui.take.data.MyMedicine
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class MainViewModel @Inject constructor(
//    private val localRepository: LocalRepository,
//    private val pharmacyRepository: PharmacyRepository
//) {
//    private val _pharmacy = MutableLiveData<List<KakaoPlacePharmacy>>()
//    val pharmacy : LiveData<List<KakaoPlacePharmacy>> get() = _pharmacy
//
//    fun getPharmacy(){
//        val getPharmacy = pharmacyRepository.getMyPharmacy()
//        _pharmacy.value = getPharmacy
//    }
//
//    fun savePharmacy(myPharmacy: List<KakaoPlacePharmacy>){
//        _pharmacy.value = myPharmacy
//        pharmacyRepository.saveMyPharmacy(myPharmacy)
//    }
//
//    fun addMyPharmacy(pharmacy: KakaoPlacePharmacy){
//            pharmacyRepository.addMyPharmacy(pharmacy)
//    }
//
//    fun isPharmacyChecked(id: String) : Boolean{
//       return pharmacyRepository.isPharmacyChecked(id)
//    }
//}