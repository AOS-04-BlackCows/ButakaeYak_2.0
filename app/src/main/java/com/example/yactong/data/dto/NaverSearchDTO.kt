package com.example.yactong.data.dto

data class NaverSearchDTO (
    // 검색어 UTF-8로 인코딩 필수
    val query: String = "약국",
    // 한번에 표시할 검색 결과 개수
    val display: Int = 50,
    // 검색 시작 위치
    val start: Int = 1,
    // 정확도
    val sort: String = "sim"
)