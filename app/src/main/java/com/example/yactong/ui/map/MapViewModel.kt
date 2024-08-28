package com.example.yactong.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yactong.data.retrofit.NaverSearchRetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
//    }
//    val text: LiveData<String> = _text
    fun getPharmacyCoordinates () {
        viewModelScope.launch(Dispatchers.IO) {
            NaverSearchRetrofitClient().fetchPharmacyCoordinates()
        }
    }
}