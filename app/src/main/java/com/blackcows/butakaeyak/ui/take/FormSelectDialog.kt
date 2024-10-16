package com.blackcows.butakaeyak.ui.take

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import com.blackcows.butakaeyak.databinding.DialogFormBinding

class FormSelectDialog(context: Context, private val listener: OnFormSelectListener) : Dialog(context) {
    private lateinit var binding : DialogFormBinding
    private val dialog = Dialog(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogFormBinding.inflate(layoutInflater)
        setCancelable(false)
        setContentView(binding.root)
        dialogSize(310, 370)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            medicineType1.setOnClickListener {
                val aImage = medicineType1.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_1", aImage)
                dismiss()
            }
            medicineType2.setOnClickListener {
                val aImage = medicineType2.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_2", aImage)
                dismiss()
            }
            medicineType3.setOnClickListener {
                val aImage = medicineType3.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_3", aImage)
                dismiss()
            }
            medicineType4.setOnClickListener {
                val aImage = medicineType4.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_4", aImage)
                dismiss()
            }
            medicineType5.setOnClickListener {
                val aImage = medicineType5.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_5", aImage)
                dismiss()
            }
            medicineType6.setOnClickListener {
                val aImage = medicineType6.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_6", aImage)
                dismiss()
            }
            medicineType7.setOnClickListener {
                val aImage = medicineType7.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_7", aImage)
                dismiss()
            }
            medicineType8.setOnClickListener {
                val aImage = medicineType8.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_8", aImage)
                dismiss()
            }
            medicineType9.setOnClickListener {
                val aImage = medicineType9.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_9", aImage)
                dismiss()
            }
            medicineType10.setOnClickListener {
                val aImage = medicineType10.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_10", aImage)
                dismiss()
            }
            medicineType11.setOnClickListener {
                val aImage = medicineType11.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_11", aImage)
                dismiss()
            }
            medicineType12.setOnClickListener {
                val aImage = medicineType12.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_12", aImage)
                dismiss()
            }
            medicineType13.setOnClickListener {
                val aImage = medicineType13.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_13", aImage)
                dismiss()
            }
            medicineType14.setOnClickListener {
                val aImage = medicineType14.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_14", aImage)
                dismiss()
            }
            medicineType15.setOnClickListener {
                val aImage = medicineType15.background
//                listener.onFormSelected(aImage)
                listener.onImageSelected("medicine_type_15", aImage)
                dismiss()
            }

        }
    }

    private fun dialogSize(width: Int, height: Int) {
        val window = window ?: return

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        val width = (displayMetrics.widthPixels * widthRatio).toInt()
//        val height = (displayMetrics.heightPixels * heightRatio).toInt()

        val density = context.resources.displayMetrics.density
        val width = (width * density).toInt()
        val height = (height * density).toInt()

        window.setLayout(width, height)
    }

    interface OnFormSelectListener {
//        fun onFormSelected(image: Drawable)
        fun onImageSelected(drawableName: String, background: Drawable)
    }
}