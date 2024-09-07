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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.home.HomeFragment
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.AlarmReceiver
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.adapter.CycleAdapter
import com.blackcows.butakaeyak.ui.take.data.AlarmItem

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

    //뒤로가기 설정
    private val onBackPressed = {
        parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.move_enter,R.anim.move_exit).remove(
            this
        ).commitNow()
    }

    //bundle에서 medicine 가져오기
    private val medicine: Medicine by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(MEDICINE_DATA)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        Log.d("CycleFragment", "${medicine.name}, ${medicine.id}")

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            adapter = CycleAdapter(requireContext(),alarmList){ itemCount ->
                btnNextUpdate(itemCount)
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }

            viewModel.getData().observe(viewLifecycleOwner, Observer {
                tvCycleName.text = "약 이름 : ${it}"
            })
            viewModel.getTextData().observe(viewLifecycleOwner, Observer{
                tvCycleForm.text = "약 모형 : " + it
            })
            viewModel.getImageData().observe(viewLifecycleOwner, Observer{
                ivCycleForm.setImageResource(it)
            })

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
            val intent = Intent(requireContext(), AlarmReceiver::class.java)

            intent.putExtra("NOTIFICATION_TITLE","${binding.tvCycleName.text}")
            intent.putExtra("NOTIFICATION_CONTENT","약 먹을 시간입니다.")
            val pendingIntent = PendingIntent.getBroadcast(
                context, alarm.requestCode, intent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12 이상에서 정확한 알람 설정
                try {
                    if (alarmManager?.canScheduleExactAlarms() == true) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            alarm.timeInMillis,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                        )
                    } else {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        startActivity(intent)
                    }
                } catch (e: SecurityException) {
                    Toast.makeText(context, "알림 설정에 실패했습니다. 권한을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Android 12 이하에서 정확한 알람 설정
                alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarm.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }

//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 이상
//                    if (alarmManager?.canScheduleExactAlarms() == true) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
//                        alarmManager.setRepeating(
//                            AlarmManager.RTC_WAKEUP,
//                            alarm.timeInMillis,
//                            AlarmManager.INTERVAL_DAY,
//                            pendingIntent
//                        )
//                    } else {
//                        // 권한 요청 화면으로 이동
//                        val requestIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
//                        startActivity(requestIntent)
//                    }
//                } else { // Android 12 이하
//                    alarmManager?.setExact(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
//                    alarmManager?.setRepeating(
//                        AlarmManager.RTC_WAKEUP,
//                        alarm.timeInMillis,
//                        AlarmManager.INTERVAL_DAY,
//                        pendingIntent
//                    )
//                }
//            } catch (e: SecurityException) {
//                // 예외 처리: Android 12 이하에서의 에러를 처리
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
//                    // Android 12 이하에서 권한 문제 발생 시 처리
//                    Toast.makeText(context, "알림 설정에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    // Android 12 이상에서 예외 발생 시 처리
//                    Toast.makeText(context, "알림 권한을 확인해 주세요.", Toast.LENGTH_SHORT).show()
//                }
//            }
        }

        Toast.makeText(context, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            CycleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}