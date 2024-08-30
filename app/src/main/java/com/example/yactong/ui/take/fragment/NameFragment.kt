package com.example.yactong.ui.take.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yactong.R
import com.example.yactong.databinding.FragmentNameBinding
import com.example.yactong.ui.take.TakeViewModel

class NameFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivDelete.setOnClickListener {
            binding.etMedicineName.text = null
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apply{
                    if(etMedicineName.length() > 0){
                        btnNext.apply{
                            isEnabled = true
                            setBackgroundResource(R.drawable.user_cl_bg_green)
                            setTextColor(Color.WHITE)
                            setOnClickListener {
                                viewModel.moveToNextPage()
                                viewModel.updateItem(etMedicineName.text.toString())
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

            override fun afterTextChanged(s: Editable?) {}
        })

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply{
                inputMethodManager.showSoftInput(etMedicineName, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}