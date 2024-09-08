package com.blackcows.butakaeyak.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.GetMedicinesNameUseCase
import com.blackcows.butakaeyak.domain.home.GetPillUseCase
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.home.data.DataSource
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchResult"
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPillUseCase: GetPillUseCase,
    private val getMedicinesNameUseCase: GetMedicinesNameUseCase,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //LiveData는 뭐 때문에 넣은거야??

    private val myMedicines = mutableListOf<MyMedicine>()

    private val _medicineResult = MutableLiveData(listOf<Medicine>())
    val medicineResult get() = _medicineResult

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun searchMedicinesWithName(name: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            _medicineResult.value = getMedicinesNameUseCase.invoke(name)
            _uiState.value = SearchUiState.SearchMedicinesSuccess(_medicineResult.value!!)
        }
    }

}