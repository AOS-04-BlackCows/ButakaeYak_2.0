package com.blackcows.butakaeyak.ui.search

import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.BottomsheetSearchDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentMedicineResultBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val TAG = "MedicineResultFragment_Log"
class MedicineResultFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentMedicineResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineAdapter : SearchRecyclerAdapter

    //viewModel 설정
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicineResultBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            medicineAdapter = SearchRecyclerAdapter(object : SearchRecyclerAdapter.ClickListener{
                override fun onItemClick(item: Medicine) {
                    val bottomSheetView = BottomsheetSearchDetailBinding.inflate(layoutInflater)
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    with(bottomSheetView){
                        Glide.with(root).load(item.imageUrl?: R.drawable.logo_big).into(detailIvMedicine)
                        detailTvName.text = item.name
                        detailTvEnterprise.text = item.enterprise
                        detailTvEffect.text = item.effect
                        detailTvInstructions.text = item.instructions
                        detailTvWarning.text = item.warning
                        detailTvCaution.text = item.caution
                        detailTvInteraction.text = item.interaction
                        detailTvSideEffect.text = item.sideEffect
                        detailTvStoringMethod.text = item.storingMethod
                    }
                    searchViewModel.saveMedicineHistory(item)
                    bottomSheetDialog.setContentView(bottomSheetView.root)
                    bottomSheetDialog.show()

                    Log.d(TAG,"${item.id}, ${item.name} ")
                }
                override fun isMedicineChecked(item: Medicine) : Boolean {
                    val result = mainViewModel.isMyMedicine(item.id!!)
                    Log.d(TAG,"${item.id}: $result")
                    return result
                }
                override fun setMedicineChecked(item: Medicine, isChecked: Boolean) {
                    val hasIt = mainViewModel.isMyMedicine(item.id!!)
                    Log.d(TAG,"Do I have item:${item.id}? :$hasIt")
                    if(hasIt) {
                        Toast.makeText(requireContext(), "이미 복용 중인 약입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        MainNavigation.addFragment(
                            TakeAddFragment.newInstance(item), FragmentTag.TakeAddFragment
                        )
                    }
                }
            })
            resultlist.adapter = medicineAdapter
            searchViewModel.medicineResult.observe(viewLifecycleOwner){
                if(it.isNotEmpty()) Log.d(TAG, "Class: ${it[0]::class.simpleName}")
                medicineAdapter.submitList(it)
            }
            resultlist.itemAnimator = null
        }
    }



    companion object {
        const val TAB_NAME = "결과 화면"
    }
}