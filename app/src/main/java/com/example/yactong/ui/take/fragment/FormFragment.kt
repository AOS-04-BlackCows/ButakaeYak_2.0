package com.example.yactong.ui.take.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yactong.R
import com.example.yactong.databinding.FragmentFormBinding
import com.example.yactong.ui.take.TakeViewModel
import com.example.yactong.ui.take.adapter.FormAdapter
import com.example.yactong.ui.take.data.FormItem

class FormFragment : Fragment(), FormAdapter.checkBoxChangeListener {

    //binding 설정
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : FormAdapter

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
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataForm = mutableListOf<FormItem>()
        dataForm.add(FormItem(R.drawable.medicine_type_3,"정제 [원형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_1,"정제 [장방형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_4,"정제 [타원형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_2,"정제 [삼각형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_5,"정제 [사각형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_6,"정제 [마름모]"))
        dataForm.add(FormItem(R.drawable.medicine_type_7,"정제 [오각형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_8,"정제 [육각형]"))
        dataForm.add(FormItem(R.drawable.medicine_type_10,"캡슐"))
        dataForm.add(FormItem(R.drawable.medicine_type_11,"주사"))
        dataForm.add(FormItem(R.drawable.medicine_type_12,"가루"))
        dataForm.add(FormItem(R.drawable.medicine_type_13,"스프레이"))
        dataForm.add(FormItem(R.drawable.medicine_type_15,"액체"))
        dataForm.add(FormItem(R.drawable.medicine_type_14,"기타"))

        binding.apply {
            ivBack.setOnClickListener {
                viewModel.moveToPreviousPage()
            }
            adapter = FormAdapter(dataForm, requireContext(), this@FormFragment)
            recyclerviewForm.adapter = adapter
            recyclerviewForm.layoutManager = LinearLayoutManager(requireContext())
            recyclerviewForm.itemAnimator = null

            viewModel.getData().observe(viewLifecycleOwner, Observer {
                tvMedicineName.text = "약 이름 : "+"${it}"
            })

            }
        }

    override fun onItemChecked(position: Int, isChecked: Boolean) {

        binding.apply {
            if (isChecked) {
                btnNext.apply {
                    val selectName = adapter.mItems[position].aName
                    isEnabled = true
                    setBackgroundResource(R.drawable.user_cl_bg_green)
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        viewModel.moveToNextPage()
                        viewModel.updateFormItem(selectName)
                    }
                }
            }
            else{
                btnNext.apply{
                    isEnabled = false
                    setBackgroundResource(R.drawable.user_cl_bg_gray)
                    setTextColor(Color.DKGRAY)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}