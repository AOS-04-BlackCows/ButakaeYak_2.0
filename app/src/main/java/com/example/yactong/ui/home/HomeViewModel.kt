package com.example.yactong.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill
import com.example.yactong.domain.home.GetPillUseCase
import com.example.yactong.ui.example.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPillUseCase: GetPillUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //LiveData는 뭐 때문에 넣은거야??

    private val _pillResult = MutableLiveData(mutableListOf<Pill>())
    private val pillResult get() = _pillResult

    fun searchPills(name: String) {
        getPillUseCase.invoke(name) { list ->
            Log.d("HomeViewModel", "SearchPills: Find ${list.size}'s result.")
            if(list.isNotEmpty()) {
                // 검색 결과 있음.
                pillResult.value = list.toMutableList()
            } else {
                // 검색 결과 없음.
            }
        }
    }

    fun getUserName(id: String) {
//        getUserNameUseCase.invoke(id) { userName ->
//            if(userName.isNotEmpty()) {
//
//            } else {
//
//            }
//        }
    }
}