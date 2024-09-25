package com.blackcows.butakaeyak.ui.user

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.viewModels
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.DialogMealTimeBinding
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.content.LocalFileContent
import java.util.Locale

@AndroidEntryPoint
class MealTimeBottomSheet : BottomSheetDialogFragment() {

    private val TAG = "MealTimeBottomSheet"

    private var _binding: DialogMealTimeBinding? = null
    private val binding get() = _binding!!

    private val mypageViewModel: MypageViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = super.onCreateDialog(savedInstanceState)

        // 커스텀 레이아웃을 인플레이트
        _binding = DialogMealTimeBinding.inflate(requireActivity().layoutInflater)
        builder.setContentView(binding.root)

        // viewModel에서 저장된 알람 시간 가져오기
        val getAlarms = mypageViewModel.getDefaultAlarms()
        Log.d(TAG, "getAlarms ${getAlarms}")
        if (getAlarms.isNotEmpty()) {
            Log.d(
                TAG,
                "breakfast : ${getAlarms[0]}, lunch : ${getAlarms[1]}, dinner : ${getAlarms[2]}"
            )
            binding.breakfastTimeTextView.text = getAlarms[0]
            binding.lunchTimeTextView.text = getAlarms[1]
            binding.dinnerTimeTextView.text = getAlarms[2]
        } else {
            Log.d(TAG, "No Alarm set.")
            // 지정된 시간이 없을 경우, 기본 시간을 설정할 수도 있다.
            binding.breakfastTimeTextView.text = "08:00"
            binding.lunchTimeTextView.text = "13:00"
            binding.dinnerTimeTextView.text = "18:00"
        }


        // 확인 버튼
        binding.btnMealSave.setOnClickListener {
            val list = mutableListOf<String>().apply {
                add(binding.breakfastTimeTextView.text.toString())
                add(binding.lunchTimeTextView.text.toString())
                add(binding.dinnerTimeTextView.text.toString())
            }

            Log.d(TAG, "Alarms to save : $list")

            mypageViewModel.saveDefaultAlarms(list)
            dialog?.dismiss()
        }

        //취소 버튼
        binding.btnMealCancel.setOnClickListener {
            dialog?.dismiss()
        }

        val setBreakfast = binding.cvBreakfastTime
        val setLunch = binding.cvLunchTime
        val setDinner = binding.cvDinnerTime

        setBreakfast.setOnClickListener {
            showTimePickerDialog(binding.breakfastTimeTextView,"08:00")
        }

        setLunch.setOnClickListener {
            showTimePickerDialog(binding.lunchTimeTextView,"13:00")
        }

        setDinner.setOnClickListener {
            showTimePickerDialog(binding.dinnerTimeTextView,"18:00")
        }

        return builder
    }

    private fun showTimePickerDialog(textView: TextView, defaultTime: String) {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.dialog_timepicker, null)

        val timePicker = dialogView.findViewById<TimePicker>(R.id.customTimePicker)
        val btnSave = dialogView.findViewById<View>(R.id.btn_save)
        val btnCancel = dialogView.findViewById<View>(R.id.btn_cancel)


        // 기본 시간 설정
        val timeParts = defaultTime.split(":")
        val defaultHour = timeParts[0].toIntOrNull() ?: 0
        val defaultMinute = timeParts[1].toIntOrNull() ?: 0

        timePicker.hour = defaultHour
        timePicker.minute = defaultMinute

        timePicker.setIs24HourView(true)

        // 다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        //저장 버튼 클릭 이벤트
        btnSave.setOnClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            textView.text = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            dialog.dismiss() // 다이얼로그 닫기
        }
        // 취소 버튼 클릭 이벤트
        btnCancel.setOnClickListener {
            dialog.dismiss() // 다이얼로그 닫기
        }

        dialog.show()
    }
}