package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill

interface DrugRepository {
    fun searchDrugs(name: String, callback: (List<Drug>) -> (Unit))
    fun searchPills(name: String, callback: (List<Pill>) -> (Unit))
}