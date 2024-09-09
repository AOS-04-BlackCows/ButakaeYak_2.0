package com.blackcows.butakaeyak.ui.take.fragment

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentNameBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment.Companion

class NameFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    //TODO: 여기!
    private val onBackPressed = {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.move_enter, R.anim.move_exit)
            .remove(this)
            .commitNow()

        MainNavigation.popCurrentFragment()
    }

    //TODO: 여기!
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

        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("NameFragment", "Back Pressed!")
                onBackPressed()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etMedicineName.setText(medicine.name!!)

            if(etMedicineName.length() > 0){
                btnNext.apply{
                    isEnabled = true
                    setBackgroundResource(R.color.green)
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        Log.d("버튼","버튼 눌림")
                        val newMedicine = medicine.copy(
                            name = binding.etMedicineName.text.toString()
                        )
                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                FormFragment.newInstance(newMedicine)
                            )
                            .addToBackStack("NameFragment")
                            .commitNow()
                    }
                }
            }
        }
        binding.ivDelete.setOnClickListener {
            binding.etMedicineName.text = null
        }

        //TODO: 여기!
        binding.ivBack.setOnClickListener {
            Log.d("NameFragment", "Back Pressed2")
            onBackPressed()
        }

        binding.etMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apply{
                    if(etMedicineName.length() > 0){
                        btnNext.apply{
                            isEnabled = true
                            setBackgroundResource(R.color.green)
                            setTextColor(Color.WHITE)
                            setOnClickListener {
                                Log.d("버튼","버튼 눌림")
                                val newMedicine = medicine.copy(
                                    name = binding.etMedicineName.text.toString()
                                )
                                parentFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.fragment_container,
                                        FormFragment.newInstance(newMedicine)
                                    )
                                    .addToBackStack("NameFragment")
                                    .commitNow()
                            }
                        }
                    }
                    else{
                        btnNext.apply{
                            isEnabled = false
                            setBackgroundResource(R.color.gray)
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

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            NameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}