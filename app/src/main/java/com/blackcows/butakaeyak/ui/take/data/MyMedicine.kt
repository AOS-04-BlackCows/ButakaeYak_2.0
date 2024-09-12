package com.blackcows.butakaeyak.ui.take.data

import com.blackcows.butakaeyak.data.models.Medicine
import io.ktor.util.date.WeekDay

/**
 * @property medicine: 약 Medicine 클래스
 * @property alarms: 해당 약에 설정되어 있는 알람들. key는 요일, value의 String 값은 시간으로 hh:mm의 형식을 갖는다.
 */
data class MyMedicine(
    val medicine: Medicine,
    val alarms: List<String>
)
