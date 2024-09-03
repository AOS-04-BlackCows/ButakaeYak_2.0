package com.example.yactong.data.models

//세부 증상 모음
data class Symptoms(val name: List<String>)

//신체 부위(중분류)
data class SearchSubCategory(
    val name: String,
    val symptoms: Symptoms
)

// 신체 부위(대분류)
enum class SearchCategory(name: String) {
    HEAD("머리"),
    FACE("얼굴"),
    NECK("목"),
    CHEST_THORAX("가슴/흉부"),
    ABDOMEN("복부"),
    BACK_WAIST("등/허리"),
    LEGS_FEET("다리/발"),
    SKIN("피부"),
}

object SearchCategoryDataSource {
    fun getSearchSubCategory(searchCategoryType: SearchCategory): List<SearchSubCategory> {
        return when (searchCategoryType) {
            SearchCategory.HEAD -> {
                listOf(
                    SearchSubCategory(
                        "두통",
                        Symptoms(
                            listOf(
                                "편두통",
                                "긴장성 두통",
                                "군발성 두통",
                                "편압성 두통",
                                "전두동통",
                                "후두통",
                                "측두동맥염에 의한 두통",
                                "약물 유발 두통",
                                "만성 일차성 두통",
                                "기후성 두통"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "어지럼증",
                        Symptoms(
                            listOf(
                                "현훈",
                                "기립성 저혈압",
                                "이석증",
                                "메니에르병",
                                "중추성 어지럼증",
                                "운동 실조",
                                "말초성 어지럼증",
                                "신경성 어지럼증",
                                "빈혈에 의한 어지럼증",
                                "저혈당에 의한 어지럼증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "시각 증상",
                        Symptoms(
                            listOf(
                                "복시",
                                "흐린 시야",
                                "시야 결손",
                                "광시증",
                                "암점",
                                "눈부심",
                                "색각 이상",
                                "사시",
                                "안구 피로",
                                "시력 저하"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "청각 및 이명",
                        Symptoms(
                            listOf(
                                "이명",
                                "청력 손실",
                                "귀울림",
                                "난청",
                                "돌발성 난청",
                                "귀 먹먹함",
                                "청각 과민증",
                                "귓속에서 물 흐르는 소리",
                                "신경성 난청",
                                "소음성 난청"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "뇌 기능 장애",
                        Symptoms(
                            listOf(
                                "기억력 감퇴",
                                "인지 기능 저하",
                                "혼란",
                                "집중력 저하",
                                "인지장애",
                                "판단력 저하",
                                "언어 장애",
                                "무감동",
                                "공간 인지 장애",
                                "실어증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "의식 변화",
                        Symptoms(
                            listOf(
                                "실신",
                                "졸음",
                                "혼미",
                                "기면증",
                                "무의식",
                                "깊은 혼수",
                                "반응 저하",
                                "의식 저하",
                                "혼동",
                                "섬망"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "안면 근육 경련 및 통증",
                        Symptoms(
                            listOf(
                                "안검 경련",
                                "삼차신경통",
                                "안면 마비",
                                "안면 경련",
                                "얼굴 찌푸림",
                                "턱 경련",
                                "근막 통증",
                                "안면 비대칭",
                                "안면 신경 손상",
                                "턱관절 통증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "두피 및 모발",
                        Symptoms(
                            listOf(
                                "두피 통증",
                                "탈모",
                                "비듬",
                                "두피 가려움증",
                                "두피 홍반",
                                "두피 여드름",
                                "모발 건조",
                                "모발 손상",
                                "두피 각질",
                                "두피 피지 분비 증가"
                            )
                        )
                    ),
                )
            }

            SearchCategory.FACE -> {
                listOf(
                    SearchSubCategory(
                        "두통",
                        Symptoms(listOf("편두통", "긴장성 두통", "군발성 두통", "전두동통", "측두동맥염에 의한 두통", "안면통"))
                    ),
                    SearchSubCategory(
                        "눈",
                        Symptoms(
                            listOf(
                                "안구건조증",
                                "결막염",
                                "안검염",
                                "녹내장",
                                "백내장",
                                "시력 저하",
                                "복시",
                                "눈부심",
                                "시야 결손",
                                "눈물샘 염증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "코",
                        Symptoms(
                            listOf(
                                "비염",
                                "코막힘",
                                "비출혈",
                                "부비동염",
                                "후각 저하",
                                "비중격만곡증",
                                "코골이",
                                "콧물",
                                "재채기",
                                "코 가려움"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "입",
                        Symptoms(
                            listOf(
                                "구내염",
                                "구강건조증",
                                "치통",
                                "잇몸 염증",
                                "구취",
                                "혀 통증",
                                "미각 이상",
                                "입술 궤양",
                                "구강 캔디다증",
                                "구강 궤양"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "귀",
                        Symptoms(
                            listOf(
                                "이명",
                                "청력 손실",
                                "귀울림",
                                "난청",
                                "귀 먹먹함",
                                "중이염",
                                "외이도염",
                                "귀 통증",
                                "귀 주위 부종",
                                "외이도 가려움"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "턱",
                        Symptoms(
                            listOf(
                                "턱관절 통증",
                                "턱 경련",
                                "턱 관절 장애",
                                "치아 교합 문제",
                                "턱 뻐근함",
                                "턱 관절 소리",
                                "턱 비대칭",
                                "턱 주변 통증",
                                "턱 근육 경직",
                                "턱 염증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "피부",
                        Symptoms(
                            listOf(
                                "여드름",
                                "건선",
                                "습진",
                                "지루성 피부염",
                                "홍조",
                                "피부 발진",
                                "피부 건조",
                                "피부 가려움",
                                "피부 감염",
                                "피부염"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "입 주변",
                        Symptoms(
                            listOf(
                                "입술 궤양",
                                "입 주변 여드름",
                                "구순염",
                                "입 주변 피부염",
                                "구강 헤르페스",
                                "입가 트임",
                                "입술 건조",
                                "입 주변 붓기",
                                "구각염",
                                "입술 갈라짐"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "안면 근육",
                        Symptoms(
                            listOf(
                                "안면 경련",
                                "안면 마비",
                                "근막 통증",
                                "안면 신경 손상",
                                "얼굴 찌푸림",
                                "턱 경련",
                                "안면 비대칭",
                                "안면 부종",
                                "안면 통증",
                                "근육 경직"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "목",
                        Symptoms(
                            listOf(
                                "인후통",
                                "편도선염",
                                "후두염",
                                "기관지염",
                                "목소리 변화",
                                "목 근육 경련",
                                "목 주변 통증",
                                "갑상선염",
                                "목 부종",
                                "목 불편감"
                            )
                        )
                    ),
                )
            }

            SearchCategory.NECK -> {
                listOf(
                    SearchSubCategory(
                        "인후통",
                        Symptoms(
                            listOf(
                                "목 통증",
                                "삼킴 곤란",
                                "인후염",
                                "편도선염",
                                "후두염",
                                "구강 건조",
                                "목 쉼",
                                "편도 비대",
                                "목이 따끔거림",
                                "후두 통증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "목 근육",
                        Symptoms(
                            listOf(
                                "목 근육 경련",
                                "목 뻐근함",
                                "목 경직",
                                "목 통증",
                                "근막 통증",
                                "근육 피로",
                                "근육염",
                                "목 디스크",
                                "목 결림",
                                "목 근육 긴장"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "갑상선",
                        Symptoms(
                            listOf(
                                "갑상선염",
                                "갑상선 비대",
                                "갑상선 기능 저하증",
                                "갑상선 기능 항진증",
                                "갑상선 결절",
                                "갑상선 통증",
                                "갑상선 부종",
                                "갑상선 종양",
                                "목 부종",
                                "목소리 변화"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "림프절",
                        Symptoms(
                            listOf(
                                "목 림프절 부종",
                                "림프절 통증",
                                "림프절염",
                                "목 부종",
                                "림프절 비대",
                                "림프절 덩어리",
                                "림프절 압통",
                                "림프절 염증",
                                "림프선염",
                                "림프절 종양"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "기도",
                        Symptoms(
                            listOf(
                                "기관지염",
                                "후두염",
                                "기도 협착",
                                "기도 통증",
                                "기도 경련",
                                "기도 건조",
                                "기도 부종",
                                "기도 염증",
                                "숨막힘",
                                "호흡 곤란"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "식도",
                        Symptoms(
                            listOf(
                                "식도염",
                                "역류성 식도염",
                                "식도 경련",
                                "삼킴 곤란",
                                "식도 통증",
                                "식도 협착",
                                "식도 궤양",
                                "식도 연축",
                                "식도 이물감",
                                "식도 부종"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "목 피부",
                        Symptoms(
                            listOf(
                                "목 피부 발진",
                                "목 피부 가려움",
                                "목 피부 건조",
                                "목 피부 염증",
                                "목 피부 감염",
                                "목 여드름",
                                "목 홍반",
                                "목 피부 발적",
                                "목 피부 벗겨짐",
                                "목 피부 두드러기"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "목혈관",
                        Symptoms(
                            listOf(
                                "경동맥 협착",
                                "경동맥 통증",
                                "목혈관 부종",
                                "목혈관 압통",
                                "목혈관 경련",
                                "목혈관 폐색",
                                "목혈관 파열",
                                "목혈관염",
                                "목혈관 협착",
                                "목혈관 동맥류"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "기타 목 증상",
                        Symptoms(
                            listOf(
                                "목의 이물감",
                                "목소리 변화",
                                "목 쉼",
                                "목소리 갈라짐",
                                "목소리 약화",
                                "목소리 소실",
                                "목소리 변성",
                                "목소리 피로",
                                "목소리 떨림",
                                "목 통증"
                            )
                        )
                    ),
                )
            }

            SearchCategory.CHEST_THORAX -> {
                listOf(
                    SearchSubCategory(
                        "흉통",
                        Symptoms(
                            listOf(
                                "가슴 통증",
                                "협심증",
                                "심근경색",
                                "늑막염",
                                "심막염",
                                "갈비뼈 골절",
                                "흉막염",
                                "가슴 뻐근함",
                                "가슴 답답함",
                                "흉부 압박감"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "호흡기 증상",
                        Symptoms(
                            listOf(
                                "호흡 곤란",
                                "천식",
                                "기관지염",
                                "기침",
                                "가슴 쌕쌕거림",
                                "과호흡",
                                "폐렴",
                                "폐색전증",
                                "호흡 불규칙",
                                "기흉"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "심장 관련 증상",
                        Symptoms(
                            listOf(
                                "심계항진",
                                "부정맥",
                                "심부전",
                                "심장 잡음",
                                "심박동 이상",
                                "심장 통증",
                                "심장 비대",
                                "심장 마비",
                                "심낭압전",
                                "심근염"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "소화기 증상",
                        Symptoms(
                            listOf(
                                "속쓰림",
                                "역류성 식도염",
                                "가슴 쓰림",
                                "소화 불량",
                                "위식도 역류",
                                "트림",
                                "가슴 타는 느낌",
                                "식도 경련",
                                "가슴 답답함",
                                "가슴 팽만감"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "근골격계 증상",
                        Symptoms(
                            listOf(
                                "가슴 근육통",
                                "늑간신경통",
                                "흉골 통증",
                                "가슴 근육 경직",
                                "가슴 근육 경련",
                                "가슴 근육 염좌",
                                "흉곽 압통",
                                "갈비뼈 염좌",
                                "가슴 근막 통증",
                                "가슴뼈 통증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "피부 증상",
                        Symptoms(
                            listOf(
                                "흉부 발진",
                                "흉부 여드름",
                                "흉부 홍반",
                                "가슴 피부 가려움",
                                "가슴 피부 건조",
                                "흉부 습진",
                                "가슴 피부염",
                                "흉부 발적",
                                "가슴 피부 두드러기",
                                "흉부 피부 감염"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "혈관 및 순환계 증상",
                        Symptoms(
                            listOf(
                                "흉부 혈관 통증",
                                "흉부 정맥류",
                                "가슴 혈관 경련",
                                "흉부 동맥류",
                                "가슴 혈관 부종",
                                "흉부 혈관 폐색",
                                "흉부 혈관염",
                                "흉부 혈관 압통",
                                "흉부 혈관 파열",
                                "흉부 혈관 협착"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "림프절",
                        Symptoms(
                            listOf(
                                "흉부 림프절 부종",
                                "흉부 림프절 통증",
                                "림프절염",
                                "흉부 림프절 비대",
                                "흉부 림프절 염증",
                                "흉부 림프절 덩어리",
                                "흉부 림프절 압통",
                                "흉부 림프절 종양",
                                "흉부 림프선염",
                                "흉부 림프절 장애"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "유방 관련 증상",
                        Symptoms(
                            listOf(
                                "유방 통증",
                                "유방 결절",
                                "유방 염증",
                                "유두 통증",
                                "유두 분비물",
                                "유방 부종",
                                "유방 울혈",
                                "유방 경직",
                                "유방 발적",
                                "유방 종양"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "기타 흉부 증상",
                        Symptoms(
                            listOf(
                                "가슴 두근거림",
                                "가슴 뛰는 느낌",
                                "흉부 답답함",
                                "흉부 압박감",
                                "흉부 부종",
                                "흉부 경직",
                                "흉부 불편감",
                                "가슴 이물감",
                                "흉부 작열감",
                                "흉부 통증"
                            )
                        )
                    ),
                )
            }

            SearchCategory.ABDOMEN -> {
                listOf(
                    SearchSubCategory(
                        "복통",
                        Symptoms(
                            listOf(
                                "상복부 통증",
                                "하복부 통증",
                                "복부 경련",
                                "위경련",
                                "복부 압통",
                                "복부 팽만감",
                                "급성 복통",
                                "만성 복통",
                                "복부 불편감",
                                "복부 쥐어짜는 느낌"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "소화기 증상",
                        Symptoms(
                            listOf(
                                "속쓰림",
                                "소화 불량",
                                "위산 역류",
                                "구토",
                                "메스꺼움",
                                "위장 팽만",
                                "위장 통증",
                                "복부 팽만감",
                                "식욕 부진",
                                "트림"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "장 관련 증상",
                        Symptoms(
                            listOf(
                                "변비",
                                "설사",
                                "장 경련",
                                "복부 가스",
                                "장이 꼬이는 느낌",
                                "장 폐색",
                                "대변 실금",
                                "복부 팽만",
                                "장내 출혈",
                                "복부 소리"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "간 및 담낭 증상",
                        Symptoms(
                            listOf(
                                "간 통증",
                                "황달",
                                "간 비대",
                                "담낭염",
                                "담석증",
                                "담도 폐색",
                                "복부 우상부 통증",
                                "간경화",
                                "담낭 통증",
                                "담즙 분비 이상"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "비뇨기계 증상",
                        Symptoms(
                            listOf(
                                "복부 하부 통증",
                                "신장 통증",
                                "방광염",
                                "요로 결석",
                                "배뇨 곤란",
                                "혈뇨",
                                "방광 통증",
                                "빈뇨",
                                "요실금",
                                "잔뇨감"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "여성 생식기 증상",
                        Symptoms(
                            listOf(
                                "생리통",
                                "자궁 통증",
                                "난소 통증",
                                "복부 하부 경련",
                                "자궁 경련",
                                "난소 낭종",
                                "복부 하부 압통",
                                "자궁출혈",
                                "복부 하부 불편감",
                                "월경 전 증후군"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "복부 피부 증상",
                        Symptoms(
                            listOf(
                                "복부 발진",
                                "복부 가려움",
                                "복부 여드름",
                                "복부 건조",
                                "복부 습진",
                                "복부 홍반",
                                "복부 피부염",
                                "복부 두드러기",
                                "복부 피부 감염",
                                "복부 피부 벗겨짐"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "혈관 및 순환계 증상",
                        Symptoms(
                            listOf(
                                "복부 혈관 통증",
                                "복부 정맥류",
                                "복부 혈관 경련",
                                "복부 동맥류",
                                "복부 혈관 부종",
                                "복부 혈관 폐색",
                                "복부 혈관염",
                                "복부 혈관 압통",
                                "복부 혈관 파열",
                                "복부 혈관 협착"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "림프절",
                        Symptoms(
                            listOf(
                                "복부 림프절 부종",
                                "복부 림프절 통증",
                                "복부 림프절염",
                                "복부 림프절 비대",
                                "복부 림프절 염증",
                                "복부 림프절 덩어리",
                                "복부 림프절 압통",
                                "복부 림프절 종양",
                                "복부 림프선염",
                                "복부 림프절 장애"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "기타 복부 증상",
                        Symptoms(
                            listOf(
                                "복부 팽만",
                                "복부 압박감",
                                "복부 부종",
                                "복부 경직",
                                "복부 불편감",
                                "복부 작열감",
                                "복부 이물감",
                                "복부 비대",
                                "복부 내장탈출",
                                "복부 통증"
                            )
                        )
                    ),
                )
            }
            //여기 아래로 뤼튼
            SearchCategory.BACK_WAIST -> {
                listOf(
                    SearchSubCategory(
                        "등",
                        Symptoms(
                            listOf(
                                "상부 등 통증",
                                "중부 등 통증",
                                "하부 등 통증",
                                "근육 긴장",
                                "척추측만증",
                                "디스크 탈출",
                                "신경 압박",
                                "염좌",
                                "골절",
                                "관절염",
                                "근막통증증후군",
                                "피로감",
                                "저림",
                                "통증의 방사",
                                "염증"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "허리",
                        Symptoms(
                            listOf(
                                "허리 통증",
                                "요통",
                                "좌골신경통",
                                "디스크 탈출",
                                "근육 긴장",
                                "관절염",
                                "신경 압박",
                                "염좌",
                                "골절",
                                "근막통증증후군",
                                "저림",
                                "통증의 방사",
                                "피로감",
                                "소변 문제",
                                "하지 무감각"
                            )
                        )
                    ),
                )
            }

            SearchCategory.LEGS_FEET -> {
                listOf(
                    SearchSubCategory(
                        "다리 통증",
                        Symptoms(
                            listOf(
                                "다리 통증",
                                "하지 저림",
                                "무감각",
                                "부종",
                                "근육 경련",
                                "정맥류",
                                "혈전증",
                                "관절염",
                                "발목 염좌",
                                "운동 시 통증 악화"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "발 통증",
                        Symptoms(
                            listOf(
                                "발 통증",
                                "발바닥 통증",
                                "발가락 통증",
                                "발의 변형",
                                "발목의 뻣뻣함",
                                "발목의 불안정성",
                                "발톱 통증",
                                "발의 피로감",
                                "발열",
                                "부풀어 오른 발"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "하지 기능 저하",
                        Symptoms(
                            listOf(
                                "신경병증",
                                "근육 약화",
                                "운동 시 다리의 불편함",
                                "다리의 무거움",
                                "다리의 떨림",
                                "자세 변화에 따른 통증",
                                "조기 피로감",
                                "다리의 경직",
                                "다리의 통증 방사",
                                "전신 피로감"
                            )
                        )
                    )
                )
            }

            SearchCategory.SKIN -> {
                listOf(
                    SearchSubCategory(
                        "피부 발진",
                        Symptoms(
                            listOf(
                                "발진",
                                "두드러기",
                                "홍반",
                                "가려움증",
                                "수포",
                                "각질",
                                "피부 자극",
                                "염증",
                                "벗겨짐",
                                "색소 침착"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "피부 감염",
                        Symptoms(
                            listOf(
                                "세균 감염",
                                "바이러스 감염",
                                "곰팡이 감염",
                                "상처 감염",
                                "농포",
                                "염증성 피부 질환",
                                "아프타성 구내염",
                                "모낭염",
                                "습진",
                                "피부 농양"
                            )
                        )
                    ),
                    SearchSubCategory(
                        "피부 건강 문제",
                        Symptoms(
                            listOf(
                                "건조증",
                                "지성 피부",
                                "여드름",
                                "피부 노화",
                                "흉터",
                                "주름",
                                "피부 탄력 저하",
                                "탈모",
                                "피부에 나타나는 변화",
                                "피부 색소 변화"
                            )
                        )
                    )
                )
            }
        }
    }
}