package com.blackcows.butakaeyak.ui.textrecognition

import com.google.gson.GsonBuilder
import retrofit2.http.Body
import retrofit2.http.POST

interface OCR_TextRecApiService {
    @POST("completions")
    suspend fun getSummarize(
        @Body requestDto: AiRequestDto
    ): OCR_ResultDto
}

data class AiRequestDto(
    val messages: List<AiMessage>,
    val temperature: Float = 0.0f,
    val model: String = "gpt-3.5-turbo"     //gpt-4o-mini   //gpt-3.5-turbo
)

data class AiMessage(
    val role: String,
    val content: String,
)

fun makeAiMessage(result: String): AiMessage {
    val role = "user"
    val prefix = "너는 긴 글에서 약 이름만 추출해 내는 역활 이야. 아래 글에서 약 이름이 없다면 '약 이름 없음' 이라고 출력하고 \n" +
            "글에서 약 이름이 존재 한다면 아래 대답 예시에 맞춰 출력해줘 \n" +
            "\n" +
            "대답 예시: 타이레놀500mg, 아픈이벤, 코푸시럽" +
            "지시 사항: 실제로 존재 하는 약 이름 인지 확인 후 출력 값에 포함 시킬 것.\n\n" +
            result

    return AiMessage(role, prefix)
}
