package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.data.CycleItem
import okhttp3.internal.notify
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
                        TimePickerDialog(requireContext(),tvCycleOneAlarmTime).show()
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
                        TimePickerDialog(requireContext(),tvCycleTwoAlarmTime).show()
                    }
                    binding.tvCycleTwoAlarm2.setOnClickListener {
                        TimePickerDialog(requireContext(),tvCycleTwoAlarmTime2).show()
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
                        TimePickerDialog(requireContext(),tvCycleThreeAlarmTime).show()
                    }
                    tvCycleThreeAlarm2.setOnClickListener {
                        TimePickerDialog(requireContext(),tvCycleThreeAlarmTime2).show()
                    }
                    tvCycleThreeAlarm3.setOnClickListener {
                        TimePickerDialog(requireContext(),tvCycleThreeAlarmTime3).show()
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

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateButtonState()
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            // TextWatcher를 TextView에 추가
            tvCycleOneAlarmTime.addTextChangedListener(textWatcher)
            tvCycleTwoAlarmTime.addTextChangedListener(textWatcher)
            tvCycleTwoAlarmTime2.addTextChangedListener(textWatcher)
            tvCycleThreeAlarmTime.addTextChangedListener(textWatcher)
            tvCycleThreeAlarmTime2.addTextChangedListener(textWatcher)
            tvCycleThreeAlarmTime3.addTextChangedListener(textWatcher)

            // 초기 상태를 업데이트
            updateButtonState()

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

    private fun updateButtonState() {
        when {
            binding.tvCycleOneAlarmTime.text.isNotBlank() -> enableButton()
            binding.tvCycleTwoAlarmTime.text.isNotEmpty() && binding.tvCycleTwoAlarmTime2.text.isNotEmpty() -> enableButton()
            binding.tvCycleThreeAlarmTime.text.isNotEmpty() && binding.tvCycleThreeAlarmTime2.text.isNotEmpty() && binding.tvCycleThreeAlarmTime3.text.isNotEmpty() -> enableButton()
            else -> disableButton()
        }
    }

    private fun enableButton() {
        binding.apply {
            btnNext.isEnabled = true
            btnNext.setBackgroundResource(R.color.green)
            btnNext.setTextColor(Color.WHITE)
            btnNext.setOnClickListener {
                saveSelectedTimes()
                val drawable = ivCycleForm.drawable
                val bitmap = drawable.let { drawableToBitmap(it) }
                var name = tvCycleName.text.toString()
                val cycleItem = CycleItem(bitmap,name)
                viewModel.updateCycleData(cycleItem)
                (requireParentFragment() as TakeAddFragment).navigateToTakeFragment()
                viewModel.resetPage()
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
        if (binding.tvCycleOneAlarmTime.text.isNotBlank()) {
            val timeParts = binding.tvCycleOneAlarmTime.text.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            viewModel.addTime(hour, minute)
        }

        if (binding.tvCycleTwoAlarmTime.text.isNotBlank() && binding.tvCycleTwoAlarmTime2.text.isNotBlank()) {
            val timeParts = binding.tvCycleTwoAlarmTime.text.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].toInt()
            val timeParts2 = binding.tvCycleTwoAlarmTime2.text.split(":")
            val hour2 = timeParts2[0].toInt()
            val minute2 = timeParts2[1].toInt()
            viewModel.addTime(hour, minute)
            viewModel.addTime(hour2, minute2)
        }

        if (binding.tvCycleThreeAlarmTime.text.isNotBlank() && binding.tvCycleThreeAlarmTime2.text.isNotBlank() &&
            binding.tvCycleThreeAlarmTime3.text.isNotBlank()) {
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