package com.blackcows.butakaeyak.ui.textrecognition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Message
import com.blackcows.butakaeyak.data.models.OCRRequest
import com.blackcows.butakaeyak.domain.repo.OCRRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OCRViewModel @Inject constructor(
    private val ocrRepository: OCRRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<GPTResultUIState>(GPTResultUIState.Loading)
    val uiState: StateFlow<GPTResultUIState> = _uiState

    fun setInit() {
        _uiState.value = GPTResultUIState.Init
    }

    fun fetchAiAnalysisResult(detail: String) {
        viewModelScope.launch {
            _uiState.value = GPTResultUIState.Loading
            try {
                val response =
                    ocrRepository.createChatCompletion(detail.createAiRequest())
                val aiResponse =
                    GPTResponse(response.choices?.get(0)?.message?.content ?: "약 이름 없음")
                _uiState.value = GPTResultUIState.Success(aiResponse)
                Log.d("medicineNameList", "OCRViewModel   GPTResponse ${GPTResponse(response.choices?.get(0)?.message?.content ?: "약 이름 없음")}")
            } catch (e: Exception) {
                _uiState.value = GPTResultUIState.Error(e.message ?: "AI 분석 실패")
            }

        }
    }
}

private fun String.createAiRequest(): OCRRequest {
    return OCRRequest(
        messages = listOf(
            Message("user",this),
            Message("system",
                "너는 긴 글에서 약 이름만 추출해 내는 역활 이야. 글에서 약 이름이 없다면 '약 이름 없음' 이라고 출력하고 \n" +
                        "글에서 약 이름이 존재 한다면 아래 대답 예시에 맞춰 출력해줘 \n" +
                        "\n" +
                        "대답 예시: 타이레놀500mg, 아픈이벤, 코푸시럽" +
                        "지시 사항: 실제로 존재 하는 약 이름 인지 확인 후 출력 값에 포함 시킬 것.\n\n"
                )
        )
    )
}