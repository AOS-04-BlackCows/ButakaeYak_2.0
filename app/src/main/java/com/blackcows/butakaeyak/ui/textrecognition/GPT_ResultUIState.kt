package com.blackcows.butakaeyak.ui.textrecognition

sealed class GPTResultUIState {
    data object Loading : GPTResultUIState()
    data class Success(val response: GPTResponse) : GPTResultUIState()
    data class Error(val errorMessage: String) : GPTResultUIState()
    data object Init: GPTResultUIState()

}

data class GPTResponse(val gptMessage: String)