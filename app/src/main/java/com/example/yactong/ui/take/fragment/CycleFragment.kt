package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import java.util.Calendar

class CycleFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentCycleBinding? = null
    private val binding get() = _binding!!

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val current = viewModel.currentPage.value ?: 0
                if (current > 0) {
                    viewModel.moveToPreviousPage()
                } else {
                    // NavController를 통해 뒤로 가기
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        _binding = FragmentCycleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ivBack.setOnClickListener {
                viewModel.moveToPreviousPage()
            }

            clCycleOne.setOnClickListener {
                if(clCycleOneAlarm.visibility == View.GONE){
                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_up_24dp)
                    clCycleOneAlarm.visibility = View.VISIBLE
                    binding.tvCycleOneAlarm.setOnClickListener {
                        showTimePickerDialog(tvCycleOneAlarmTime)
                    }

                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                    tvCycleTwoAlarmTime.text = ""
                    tvCycleTwoAlarmTime2.text = ""

                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
                    tvCycleThreeAlarmTime.text = ""
                    tvCycleThreeAlarmTime2.text = ""
                    tvCycleThreeAlarmTime3.text = ""
                }
                else{
                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                    tvCycleOneAlarmTime.text = ""
                }
            }

            clCycleTwo.setOnClickListener {
                if(clCycleTwoAlarm.visibility == View.GONE){
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_up_24dp)
                    clCycleTwoAlarm.visibility = View.VISIBLE
                    binding.tvCycleTwoAlarm.setOnClickListener {
                        showTimePickerDialog(tvCycleTwoAlarmTime)
                    }
                    binding.tvCycleTwoAlarm2.setOnClickListener {
                        showTimePickerDialog(tvCycleTwoAlarmTime2)
                    }

                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                    tvCycleOneAlarmTime.text = ""

                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
                    tvCycleThreeAlarmTime.text = ""
                    tvCycleThreeAlarmTime2.text = ""
                    tvCycleThreeAlarmTime3.text = ""
                }
                else{
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                    tvCycleTwoAlarmTime.text = ""
                    tvCycleTwoAlarmTime2.text = ""
                }
            }

            clCycleThree.setOnClickListener {
                if(clCycleThreeAlarm.visibility == View.GONE){
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_up_24dp)
                    clCycleThreeAlarm.visibility = View.VISIBLE
                    tvCycleThreeAlarm.setOnClickListener {
                        showTimePickerDialog(tvCycleThreeAlarmTime)
                    }
                    tvCycleThreeAlarm2.setOnClickListener {
                        showTimePickerDialog(tvCycleThreeAlarmTime2)
                    }
                    tvCycleThreeAlarm3.setOnClickListener {
                        showTimePickerDialog(tvCycleThreeAlarmTime3)
                    }

                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                    tvCycleTwoAlarmTime.text = ""
                    tvCycleTwoAlarmTime2.text = ""

                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                    tvCycleOneAlarmTime.text = ""
                }
                else{
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
                    tvCycleThreeAlarmTime.text = ""
                    tvCycleThreeAlarmTime2.text = ""
                    tvCycleThreeAlarmTime3.text = ""
                }
            }

            btnNext.setOnClickListener {
                if (tvCycleOneAlarmTime.text != "") {
                    btnNext.isEnabled = true
                    saveSelectedTimes()
                }
            }

            viewModel.getData().observe(viewLifecycleOwner, Observer {
                tvCycleName.text = "약 이름 : ${it}"
            })

            viewModel.getTextData().observe(viewLifecycleOwner, Observer{
                tvCycleForm.text = "약 모형 : " + it
            })
        }
    }

    private fun showTimePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerView = layoutInflater.inflate(R.layout.dialog_timepicker, null)
        val timePicker = timePickerView.findViewById<TimePicker>(R.id.customTimePicker)
        val save = timePickerView.findViewById<Button>(R.id.btn_save)
        val cancle = timePickerView.findViewById<Button>(R.id.btn_cancle)

        // 시간 초기화
        timePicker.hour = hour
        timePicker.minute = minute

        //다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setView(timePickerView)
            .create()

        cancle.setOnClickListener {
            dialog.cancel()
        }

        save.setOnClickListener {
            // 사용자가 선택한 시간 가져오기
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            textView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            dialog.dismiss()
        }

        // 타이틀 제거
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        // 시간 변경 리스너 설정
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            // 타이틀 업데이트
            dialog.setTitle(String.format("%02d:%02d", hourOfDay, minute))
        }

        dialog.show()
    }



    private fun saveSelectedTimes() {
        // TextView에 입력된 시간 중 유효한 값만 ViewModel에 저장
        if (binding.tvCycleOneAlarmTime.text != "") {
            val timeParts = binding.tvCycleOneAlarmTime.text.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            viewModel.addTime(hour, minute)
        }

        if (binding.tvCycleTwoAlarmTime.text != "" && binding.tvCycleTwoAlarmTime2.text != "") {
            val timeParts = binding.tvCycleTwoAlarmTime.text.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val timeParts2 = binding.tvCycleTwoAlarmTime2.text.split(":")
            val hour2 = timeParts2[0].toInt()
            val minute2 = timeParts2[1].toInt()
            viewModel.addTime(hour, minute)
            viewModel.addTime(hour2, minute2)
        }

        if (binding.tvCycleThreeAlarmTime.text != "" && binding.tvCycleThreeAlarmTime2.text != "" &&
            binding.tvCycleThreeAlarmTime3.text != "") {
            val timeParts = binding.tvCycleThreeAlarmTime.text.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val timeParts2 = binding.tvCycleThreeAlarmTime2.text.split(":")
            val hour2 = timeParts2[0].toInt()
            val minute2 = timeParts2[1].toInt()
            val timeParts3 = binding.tvCycleThreeAlarmTime3.text.split(":")
            val hour3 = timeParts3[0].toInt()
            val minute3 = timeParts3[1].toInt()
            viewModel.addTime(hour, minute)
            viewModel.addTime(hour2, minute2)
            viewModel.addTime(hour3, minute3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}