package com.blackcows.butakaeyak.ui.home.data

import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.blackcows.butakaeyak.data.models.Medicine
import org.junit.runner.manipulation.Ordering.Context

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

        //SharedPreferences
        fun saveData(context: android.content.Context, key:String, data: Set<String>) {
            val edit = context?.getSharedPreferences("pref", MODE_PRIVATE)?.edit()
            edit!!.putStringSet(key,data)
            edit!!.apply() // 저장완료
        }

//        fun loadData() {
//            val pref = getSharedPreferences("pref", MODE_PRIVATE)
//            binding.mainEtTitleEdit.setText(pref.getString("name",""))
//        }
    }

    // MVVM패턴에서 Model에 해당한다고 볼 수 있음
    fun getPillList() : List<Medicine>{
        // 만들어놓은 데이터클래스 리턴
        return CardDataList()
    }
    fun CardDataList() : ArrayList<Medicine>{
        return arrayListOf(
            Medicine(
                "더미 1",
                "더미덤",
                "000001",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 2",
                "더미덤",
                "000002",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 3",
                "더미덤",
                "000003",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 4",
                "더미덤",
                "000004",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 5",
                "더미덤",
                "000005",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 6",
                "더미덤",
                "000006",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 7",
                "더미덤",
                "000007",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 8",
                "더미덤",
                "000008",
                "약약",
                "초코"
            ),
            Medicine(
                "더미 9",
                "더미덤",
                "000009",
                "약약",
                "초코"
            ),
            Medicine(
                "더미10",
                "더미덤",
                "000010",
                "약약",
                "초코"
            ),
            Medicine(
                "더미11",
                "더미덤",
                "000011",
                "약약",
                "초코"
            ),
            Medicine(
                "더미12",
                "더미덤",
                "000012",
                "약약",
                "초코"
            ),
            Medicine(
                "더미13",
                "더미덤",
                "000013",
                "약약",
                "초코"
            ),
            Medicine(
                "더미14",
                "더미덤",
                "000014",
                "약약",
                "초코"
            ),
            Medicine(
                "더미15",
                "더미덤",
                "000015",
                "약약",
                "초코"
            ),
            Medicine(
                "더미16",
                "더미덤",
                "000016",
                "약약",
                "초코"
            ),
            Medicine(
                "더미17",
                "더미덤",
                "000017",
                "약약",
                "초코"
            ),
            Medicine(
                "더미18",
                "더미덤",
                "000018",
                "약약",
                "초코"
            ),
            Medicine(
                "더미19",
                "더미덤",
                "000019",
                "약약",
                "초코"
            ),
            Medicine(
                "더미20",
                "더미덤",
                "000020",
                "약약",
                "초코"
            ),
            Medicine(
                "더미21",
                "더미덤",
                "000021",
                "약약",
                "초코"
            ),
            Medicine(
                "더미22",
                "더미덤",
                "000022",
                "약약",
                "초코"
            ),
            Medicine(
                "더미23",
                "더미덤",
                "000023",
                "약약",
                "초코"
            ),
            Medicine(
                "더미24",
                "더미덤",
                "000024",
                "약약",
                "초코"
            ),
            Medicine(
                "더미25",
                "더미덤",
                "000025",
                "약약",
                "초코"
            ),
            Medicine(
                "더미26",
                "더미덤",
                "000026",
                "약약",
                "초코"
            ),
            Medicine(
                "더미27",
                "더미덤",
                "000027",
                "약약",
                "초코"
            ),
            Medicine(
                "더미28",
                "더미덤",
                "000028",
                "약약",
                "초코"
            ),
            Medicine(
                "더미29",
                "더미덤",
                "000029",
                "약약",
                "초코"
            ),
        )
    }
}