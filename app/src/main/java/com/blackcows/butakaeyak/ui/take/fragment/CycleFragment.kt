package com.blackcows.butakaeyak.ui.take.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.adapter.CycleAdapter
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import com.blackcows.butakaeyak.ui.take.data.CycleItem

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
            adapter = CycleAdapter(alarmList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()

            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }

            // 초기 상태를 업데이트
            updateButton()

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
    }

    private fun updateButton() {
        when {
//            binding.tvCycleOneAlarmTime.text.isNotEmpty() -> enableButton()
            else -> disableButton()
        }
    }

    private fun enableButton() {
        binding.apply {
            btnNext.isEnabled = true
            btnNext.setBackgroundResource(R.color.green)
            btnNext.setTextColor(Color.WHITE)
            btnNext.setOnClickListener {
                val selectedTimes = mutableListOf<Pair<Int, Int>>()

                // ViewModel에 시간 추가
                selectedTimes.forEach { time ->
                    viewModel.addTime(time.first, time.second)
                }

                val drawable = ivCycleForm.drawable
                val bitmap = drawable.let { drawableToBitmap(it) }
                var name = tvCycleName.text.toString()
                val cycleItem = CycleItem(bitmap,name)
                viewModel.updateCycleData(cycleItem)
                (requireParentFragment() as TakeAddFragment).navigateToTakeFragment()
                viewModel.resetPage()
//                saveSelectedTimes()
            }
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun disableButton() {
        binding.apply {
            btnNext.isEnabled = false
            btnNext.setBackgroundResource(R.color.gray)
            btnNext.setTextColor(Color.DKGRAY)
        }
    }

    private fun saveSelectedTimes() {
        // TextView에 입력된 시간 중 유효한 값만 ViewModel에 저장
//        if (binding.tvCycleOneAlarmTime.text.isNotBlank()) {
//            val timeParts = binding.tvCycleOneAlarmTime.text.split(":")
//            val hour = timeParts[0].toInt()
//            val minute = timeParts[1].toInt()
//            viewModel.addTime(hour, minute)
//        }
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
            }
        }
        timePickerDialog.show()
    }

    private fun addAlarmItem(timeText: String) {
        val newAlarm = AlarmItem(
            timeText,
            false, false, false, false,
            false, false, false,
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.dark_gray)
        )
        alarmList.add(newAlarm)
        adapter.notifyItemInserted(alarmList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}