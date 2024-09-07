package com.blackcows.butakaeyak.ui.home

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.ui.example.UserUiState

sealed class SearchUiState {
    data class SearchMedicinesSuccess(val medicines: List<Medicine>): SearchUiState()

    data object Loading: SearchUiState()
    data object Init: SearchUiState()
}