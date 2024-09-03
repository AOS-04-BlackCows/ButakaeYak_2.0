package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill

interface DrugRepository {
    fun searchDrugs(name: String, callback: (List<Drug>) -> (Unit))
    fun searchPills(name: String, callback: (List<Pill>) -> (Unit))
}