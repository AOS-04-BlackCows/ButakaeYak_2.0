package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.ui.take.data.MyMedicine

interface LocalRepository {
    fun getMyMedicines(): List<MyMedicine>      // 저장되어 있는 모든 MyMedicine 가져오기
    fun saveMyMedicines(list: List<MyMedicine>) // 파라미터 안 모든 MyMedicines 덮어써서 저장
    fun addToMyMedicine(myMedicine: MyMedicine) // MyMedicine 하나 추가하여 저장

    fun isMyMedicine(id: String): Boolean       // 내가 복용중인 약인지 아닌지
    @Deprecated("이거 쓰지말고 LiveData에서 value 값 변경 후 save 호출하기")
    fun cancelMyMedicine(id: String)            // MyMedicine에서 삭제
}