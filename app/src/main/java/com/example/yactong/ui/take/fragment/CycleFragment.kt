package com.example.yactong.ui.take.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.yactong.R
import com.example.yactong.databinding.FragmentCycleBinding
import com.example.yactong.ui.take.TakeViewModel

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
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
                }
                else{
                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                }
            }

            clCycleTwo.setOnClickListener {
                if(clCycleTwoAlarm.visibility == View.GONE){
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_up_24dp)
                    clCycleTwoAlarm.visibility = View.VISIBLE
                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
                }
                else{
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                }
            }

            clCycleThree.setOnClickListener {
                if(clCycleThreeAlarm.visibility == View.GONE){
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_up_24dp)
                    clCycleThreeAlarm.visibility = View.VISIBLE
                    ivArrow2.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleTwoAlarm.visibility = View.GONE
                    ivArrow1.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleOneAlarm.visibility = View.GONE
                }
                else{
                    ivArrow3.setImageResource(R.drawable.baseline_arrow_circle_down_24dp)
                    clCycleThreeAlarm.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}