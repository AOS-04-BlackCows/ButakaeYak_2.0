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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if(fragmentTag == FragmentTag.CycleFragmentInHome) {
            MainNavigation.popCurrentFragment()
        } else {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.move_enter,R.anim.move_exit)
                .remove(this)
                .commitNow()
        }
    }

    //bundle에서 medicine 가져오기
    private val myMedicine: MyMedicine by lazy {
        val medicine = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(MEDICINE_DATA)!!
        }

        mainViewModel.getMyMedicineOnList(medicine.id!!) ?: MyMedicine(medicine, listOf())
    }
    //bundle에서 medicine 가져오기
    private val fragmentTag by lazy {
        when(arguments?.getString(FRAGMENT_TAG)!!) {
            FragmentTag.CycleFragmentInHome.name -> FragmentTag.CycleFragmentInHome
            FragmentTag.CycleFragmentInTakeAdd.name -> FragmentTag.CycleFragmentInTakeAdd
            else -> null
        }!!
    }

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

        Log.d("CycleFragment", "${myMedicine.medicine.name}, ${myMedicine.medicine.id}")

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
            }

            clRepeatCycle.setOnClickListener {
                bottomSheetDialog2.show()
            }

            if (myMedicine.alarms.isNotEmpty()) {
                alarmList.clear() // 기존 알람 리스트 초기화
                Log.d("CycleFragment", "알람 시간: ${myMedicine.alarms}")

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

            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }

//            tvCycleName.text = "약 이름 : ${myMedicine.medicine.name}"


        }
        btnNextUpdate(adapter.itemCount)
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
        binding.btnNext.isEnabled = itemCount > 0
        if (itemCount > 0) {
            binding.btnNext.setBackgroundResource(R.color.green)
            binding.btnNext.setTextColor(Color.WHITE)
            binding.btnNext.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .remove(this@CycleFragment)
                    .commit()

                setAlarmForAllItems()
                val newMyMedicine = myMedicine.copy(
                    alarms = alarmList.map {
                        it.timeText!!
                    }
                )
                mainViewModel.addToMyMedicineList(newMyMedicine)
                MainNavigation.popCurrentFragment()
            }
        } else {
            binding.btnNext.setBackgroundResource(R.color.gray)
            binding.btnNext.setTextColor(Color.DKGRAY)
            binding.btnNext.setOnClickListener {
                Toast.makeText(context, "시간 설정이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        if(fragmentTag == FragmentTag.CycleFragmentInHome) {
            MainNavigation.hideBottomNavigation(false)
        }
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
    }
}