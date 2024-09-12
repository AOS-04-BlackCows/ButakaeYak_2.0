package com.blackcows.butakaeyak.ui.take

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.widget.TextView
import com.blackcows.butakaeyak.databinding.DialogTimepickerBinding
import java.util.Calendar

class TimePickerDialog(private val context: Context, private val textView : TextView) : Dialog(context) {
    private lateinit var binding : DialogTimepickerBinding
    private val dialog = Dialog(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogTimepickerBinding.inflate(layoutInflater)
        setCancelable(false)
        setContentView(binding.root)
        dialogSize(0.75f, 0.4f)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 타이틀 제거
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = binding.customTimePicker
        val save = binding.btnSave
        val cancel = binding.btnCancel

        // 시간 초기화
        timePicker.hour = hour
        timePicker.minute = minute

        cancel.setOnClickListener {
            cancel()
        }

        save.setOnClickListener {
            // 사용자가 선택한 시간 가져오기
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            textView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            dismiss()
        }
    }

    private fun dialogSize(widthRatio: Float, heightRatio: Float) {
        val window = window ?: return

        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = (displayMetrics.widthPixels * widthRatio).toInt()
        val height = (displayMetrics.heightPixels * heightRatio).toInt()

        window.setLayout(width, height)
    }
}