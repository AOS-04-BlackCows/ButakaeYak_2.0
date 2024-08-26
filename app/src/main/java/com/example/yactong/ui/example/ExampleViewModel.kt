package com.example.yactong.ui.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yactong.domain.example.GetUserNameUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun getUserName(id: String) {
        getUserNameUseCase.invoke(id) { userName ->
            if(userName.isNotEmpty()) {
                _uiState.value = UserUiState.GetUserNameSuccess(userName)
            } else {
                _uiState.value = UserUiState.Failure
            }
        }
    }
}