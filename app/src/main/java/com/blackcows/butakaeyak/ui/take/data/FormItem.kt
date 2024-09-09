package com.blackcows.butakaeyak.ui.take.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormItem(var aImage : Int, var aName : String):Parcelable

@Parcelize
data class CycleItem(val aImage : Bitmap, val aName : String):Parcelable

data class AlarmItem(
    var timeText: String? = null,
    var timeInMillis: Long = 0L,
    val requestCode: Int = (System.currentTimeMillis() % Integer.MAX_VALUE).toInt()
)
