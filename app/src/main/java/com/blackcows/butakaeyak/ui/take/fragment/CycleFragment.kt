package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.BottomsheetCalendarBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMapDetailBinding
import com.blackcows.butakaeyak.databinding.BottomsheetRepeatCycleBinding
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.AlarmReceiver
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.adapter.CycleAdapter
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class CycleFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentCycleBinding? = null
    private val binding get() = _binding!!

    //adapter 설정
    private lateinit var adapter : CycleAdapter

    //data class
    private val alarmList = mutableListOf<AlarmItem>()

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var bottomSheetView: BottomsheetCalendarBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var bottomSheetView2: BottomsheetRepeatCycleBinding
    private lateinit var bottomSheetDialog2: BottomSheetDialog

    //뒤로가기 설정
    private val onBackPressed = {
            MainNavigation.popCurrentFragment()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.move_enter,R.anim.move_exit)
                .remove(this)
                .commitNow()
    }

    private val myMedicine : MyMedicine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        MainNavigation.hideBottomNavigation(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })

        _binding = FragmentCycleBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetView = BottomsheetCalendarBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView.root)

        bottomSheetView2 = BottomsheetRepeatCycleBinding.inflate(layoutInflater)
        bottomSheetDialog2 = BottomSheetDialog(requireContext())
        bottomSheetDialog2.setContentView(bottomSheetView2.root)
        bottomSheetDialog2.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.etMedicineGroup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnNext()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }

            adapter = CycleAdapter(requireContext(),alarmList){ itemCount ->
                btnNextUpdate(itemCount)
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            clStartDay.setOnClickListener {
                bottomSheetDialog.show()
                    bottomSheetView.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                        val selectDate = "$year-${month + 1}-$dayOfMonth"
                        binding.tvStartDay.setText(selectDate)
                        btnNext()
                    }
            }

            binding.clRepeatCycle.setOnClickListener {
                bottomSheetDialog2.show()

                val selectedDays = mutableListOf<String>()

                bottomSheetView2.tvMonday.setOnClickListener {
                    handleDaySelection(it as TextView, "월요일", selectedDays)
                }

                bottomSheetView2.tvTuesday.setOnClickListener {
                    handleDaySelection(it as TextView, "화요일", selectedDays)
                }

                bottomSheetView2.tvWednesday.setOnClickListener {
                    handleDaySelection(it as TextView, "수요일", selectedDays)
                }

                bottomSheetView2.tvThursday.setOnClickListener {
                    handleDaySelection(it as TextView, "목요일", selectedDays)
                }

                bottomSheetView2.tvFriday.setOnClickListener {
                    handleDaySelection(it as TextView, "금요일", selectedDays)
                }

                bottomSheetView2.tvSaturday.setOnClickListener {
                    handleDaySelection(it as TextView, "토요일", selectedDays)
                }

                bottomSheetView2.tvSunday.setOnClickListener {
                    handleDaySelection(it as TextView, "일요일", selectedDays)
                }

                bottomSheetView2.btnNext.setOnClickListener {
                    if(bottomSheetView2.etCustom.text.isNotEmpty()){
                        binding.tvRepeatCycle.text = "${bottomSheetView2.etCustom.text}일"
                        bottomSheetView2.tvCustomCaption.setText("${bottomSheetView2.etCustom.text}일마다 반복합니다.")
                    }
                    else {
                        val orderedDays = orderSelectedDays(selectedDays)
                        val selectedDaysString = orderedDays.joinToString(", ")
                        binding.tvRepeatCycle.text = selectedDaysString
                    }
                    bottomSheetDialog2.dismiss()
                    btnNext()
                }

                bottomSheetDialog2.setOnDismissListener {
                    bottomSheetView2.etCustom.setText("")

                    bottomSheetView2.tvMonday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvTuesday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvWednesday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvThursday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvFriday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvSaturday.setBackgroundResource(android.R.color.white)
                    bottomSheetView2.tvSunday.setBackgroundResource(android.R.color.white)

                    bottomSheetView2.tvMonday.isSelected = false
                    bottomSheetView2.tvTuesday.isSelected = false
                    bottomSheetView2.tvWednesday.isSelected = false
                    bottomSheetView2.tvThursday.isSelected = false
                    bottomSheetView2.tvFriday.isSelected = false
                    bottomSheetView2.tvSaturday.isSelected = false
                    bottomSheetView2.tvSunday.isSelected = false
                }
            }

            myMedicine?.let {
                if (myMedicine.alarms.isNotEmpty()) {
                    alarmList.clear() // 기존 알람 리스트 초기화

                    for (alarmString in myMedicine.alarms) {
                        // 시간 문자열 포맷이 맞는지 확인
                        val timeParts = alarmString.split(":")
                        if (timeParts.size == 2) {
                            try {
                                val hour = timeParts[0].toInt()
                                val minute = timeParts[1].toInt()
                                val alarmItem = AlarmItem(
                                    timeText = alarmString,
                                    timeInMillis = adapter.getTimeInMillis(hour, minute)
                                )
                                alarmList.add(alarmItem)
                                Log.d("CycleFragment", "Added alarm item: $alarmItem")
                            } catch (e: NumberFormatException) {
                                Log.e("CycleFragment", "Error parsing time: $alarmString", e)
                            }
                        } else {
                            Log.e("CycleFragment", "Invalid time format: $alarmString")
                        }
                    }

                    adapter.notifyDataSetChanged() // 어댑터에 변경사항 알림
                } else {
                    Log.e("CycleFragment", "No alarms found")
                }
            }

            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }
        }
        btnNextUpdate(adapter.itemCount)
    }

    private fun orderSelectedDays(selectedDays: List<String>): List<String> {
        val dayOrder = listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")

        return selectedDays.sortedBy { dayOrder.indexOf(it) }
    }

    private fun showCustomTimePickerDialog() {
        // text 임시 설정
        val tempTextView = TextView(requireContext())

        // 커스텀 TimePickerDialog 생성 및 보여주기
        val timePickerDialog = TimePickerDialog(requireContext(), tempTextView)
        timePickerDialog.setOnDismissListener {
            val selectedTime = tempTextView.text.toString()
            if (selectedTime.isNotEmpty()) {
                addAlarmItem(selectedTime)
                btnNextUpdate(adapter.itemCount)
            }
        }
        timePickerDialog.show()
    }

    private fun addAlarmItem(timeText: String) {
        val timeParts = timeText.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val alarmItem = AlarmItem(
            timeText = timeText,
            timeInMillis = adapter.getTimeInMillis(hour, minute)
        )
        adapter.addItem(alarmItem)
    }

    private fun btnNextUpdate(itemCount: Int) {
        if(itemCount>0) {
            binding.tvCycleAlarmAdd.setText("${itemCount}개 설정")
        }
        else{
            binding.tvCycleAlarmAdd.text = ""
        }
        btnNext()
    }

    private fun btnNext(){

        val isMedicineGroup = binding.etMedicineGroup.text.isNotEmpty()
        val isStartDay = binding.tvStartDay.text.isNotEmpty()
        val isRepeatCycle = binding.tvRepeatCycle.text.isNotEmpty()
        val isCycleAlarmAdd = binding.tvCycleAlarmAdd.text.isNotEmpty()

        binding.apply {
            if(isMedicineGroup && isStartDay && isRepeatCycle && isCycleAlarmAdd){
                btnNext.isEnabled = true
                btnNext.setBackgroundResource(R.color.green)
                btnNext.setTextColor(Color.WHITE)
                btnNext.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .remove(this@CycleFragment)
                        .commit()

                    setAlarmForAllItems()
                    val newMyMedicine = myMedicine?.copy(
                        alarms = alarmList.map {
                            it.timeText!!
                        }
                    )
                    newMyMedicine?.let { it1 -> mainViewModel.addToMyMedicineList(it1) }
                    MainNavigation.popCurrentFragment()
                }
            }
            else{
                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.color.gray)
                btnNext.setTextColor(Color.DKGRAY)
                btnNext.setOnClickListener {
                Toast.makeText(context, "시간 설정이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun setAlarmForAllItems() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmList = adapter.getAlarmList()

        for (alarm in alarmList) {
            Log.d("timeInMillis","${alarm.timeInMillis}")
            Log.d("requestCode","${alarm.requestCode}")
            val intent = Intent(requireContext(), AlarmReceiver::class.java)

            intent.putExtra("NOTIFICATION_ID", alarm.requestCode)
            intent.putExtra("NOTIFICATION_TITLE","${binding.etMedicineGroup.text}")
            intent.putExtra("NOTIFICATION_CONTENT","약 먹을 시간입니다.")

            val pendingIntent = PendingIntent.getBroadcast(
                context, alarm.requestCode, intent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12 이상에서 정확한 알람 설정
                try {
                    if (alarmManager?.canScheduleExactAlarms() == true) {
                        val alarmClock = AlarmManager.AlarmClockInfo(alarm.timeInMillis, pendingIntent)
                        alarmManager.setAlarmClock(alarmClock, pendingIntent)
                    } else {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        startActivity(intent)
                    }
                } catch (e: SecurityException) {
                    Toast.makeText(context, "알림 설정에 실패했습니다. 권한을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        Toast.makeText(context, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun handleDaySelection(textView: TextView, day: String, selectedDays: MutableList<String>) {
        if (textView.isSelected) {
            textView.setBackgroundResource(android.R.color.white)
            selectedDays.remove(day)
        } else {
            textView.setBackgroundResource(R.drawable.user_cl_bg_green_gradient_gray)
            selectedDays.add(day)
        }
        textView.isSelected = !textView.isSelected
    }

    override fun onDestroyView() {
        super.onDestroyView()
            MainNavigation.hideBottomNavigation(false)
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"
        private const val FRAGMENT_TAG = "FRAGMENT_TAG"

        @JvmStatic
        fun newInstance(medicine: Medicine, fragmentTag: FragmentTag) =
            CycleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                    putString(FRAGMENT_TAG, fragmentTag.name)
                }
            }

        @JvmStatic
        fun newInstance() = CycleFragment()
    }
}