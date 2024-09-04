package com.blackcows.butakaeyak.ui.take.data

import android.graphics.Bitmap
import android.os.Parcelable
import android.widget.ImageView
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormItem(var aImage : Int, var aName : String):Parcelable

@Parcelize
data class CycleItem(var aImage : Bitmap, var aName : String):Parcelable
