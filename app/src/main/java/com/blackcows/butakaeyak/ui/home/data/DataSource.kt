package com.blackcows.butakaeyak.ui.home.data

class DataSource {
    companion object{
        private var INSTANCE : DataSource? = null

        fun getDataSoures() : DataSource{
            // DatoSource::class 객체에 lock을 걸어 한번에 한 스레드에서만 실행 되도록 함
            return synchronized(DataSource::class) {
                // 싱글톤 객체를 한번 호출하고 없으면 DataSource반환, 있으면 생성된 인스턴스 반환
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    // MVVM패턴에서 Model에 해당한다고 볼 수 있음
    fun getPillList() : List<ListItem.PillResultItem>{
        // 만들어놓은 데이터클래스 리턴
        return CardDataList()
    }
    fun CardDataList() : ArrayList<ListItem.PillResultItem>{
        return arrayListOf(
            ListItem.PillResultItem(
                "더미 1",
                "더미덤",
                "000001",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 2",
                "더미덤",
                "000002",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 3",
                "더미덤",
                "000003",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 4",
                "더미덤",
                "000004",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 5",
                "더미덤",
                "000005",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 6",
                "더미덤",
                "000006",
                "약약",
                "초코"
            ),
            ListItem.PillResultItem(
                "더미 7",
                "더미덤",
                "000007",
                "약약",
                "초코"
            ),
        )
    }
}