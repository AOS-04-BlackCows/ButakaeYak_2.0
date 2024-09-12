package com.blackcows.butakaeyak.domain.repo

import android.graphics.Bitmap

interface FirebaseImageRepository {
    suspend fun uploadProfile(bitmap: Bitmap)
    suspend fun deleteProfile(id: String)
}