package com.blackcows.butakaeyak.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.GetMedicinesNameUseCase
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchResult"
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMedicinesNameUseCase: GetMedicinesNameUseCase,
    private val localUtilsRepository: LocalUtilsRepository
) : ViewModel() {

    private val _queryHistory = MutableLiveData<List<String>>()
    val queryHistory get() = _queryHistory
    private val _viewedHistory = MutableLiveData<List<Medicine>>()
    val viewedHistory get() = _viewedHistory

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //뷰페이저의 현재 아이템(화면)용

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

    fun getQueryHistory(){
        //TODO 검색한 기록 저장 (반환타입 List<String>)
//        queryHistory.value = localUtilsRepository.getQueryHistory()
        _queryHistory.apply {
            value = listOf("코펜","타이레놀","사방정","뉴프")
        }
    }

    fun getViewedHistory(){
        //TODO 검색후 클릭한 기록 저장 (반환타입 List<Medicine>)
//        queryHistory.value = localUtilsRepository.getViewedViewedHistory()
//        queryHistory.value = localUtilsRepository.getViewedViewedHistory()
        _viewedHistory.apply {
            value = listOf(
                Medicine(name = "알코펜연질켑슐",effect = "감기에 좋음",imageUrl = ""),
                Medicine(name = "알코펜",effect = "감기에 좋음?",imageUrl = ""),
                Medicine(name = "알코켑슐",effect = "감기에 좋음!",imageUrl = ""),
            )
        }
    }
}