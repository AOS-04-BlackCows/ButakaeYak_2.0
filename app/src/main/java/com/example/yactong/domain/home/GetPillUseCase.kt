package com.example.yactong.domain.home

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill
import com.example.yactong.data.repository.DrugRepository
import javax.inject.Inject

class GetPillUseCase @Inject constructor(
    private val drugRepository: DrugRepository  //TODO: 이용하는 Repository
) {
    //TODO: 무조건 Invoke를 이용한다.
    operator fun invoke(
        name: String,     //필요한 프로퍼티 넣기
        callback: (List<Pill>) -> (Unit)) {     // 람다로 callback 함수 넣기
        drugRepository.searchPills(name).onSuccess{}.onFailure {
            listOf<Pill>()
        }
    }
}