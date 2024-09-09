package com.blackcows.butakaeyak.ui.take.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentFormBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.adapter.FormAdapter
import com.blackcows.butakaeyak.ui.take.data.FormItem
import com.blackcows.butakaeyak.ui.take.fragment.NameFragment.Companion

class FormFragment : Fragment(), FormAdapter.checkBoxChangeListener {

    //binding 설정
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : FormAdapter

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    //뒤로가기 설정
    private val onBackPressed = {
        parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.move_enter,R.anim.move_exit).remove(
            this
        ).commitNow()
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
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
            ivBack.setOnClickListener{
                onBackPressed()
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
                    val selectImage = adapter.mItems[position].aImage
                    isEnabled = true
                    setBackgroundResource(R.color.green)
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,
                                    CycleFragment.newInstance(medicine)
                            ).commitNow()


                        viewModel.updateFormItem(selectName)
                        viewModel.updateFormImageItem(selectImage)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            FormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}