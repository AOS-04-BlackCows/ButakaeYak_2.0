package com.blackcows.butakaeyak.ui.search

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.BottomsheetSearchDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchHistoryBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.search.adapter.HistoryRecyclerAdapter
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

private const val TAG = "검색 기록"
class SearchHistoryFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentSearchHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: SearchViewModel by activityViewModels()
    private lateinit var historyAdapter : HistoryRecyclerAdapter

    private var columnCount = 1 //컬럼 갯수 = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchHistoryDelete.setOnClickListener {
            historyViewModel.removeQueryHistory()
            binding.searchHistoryChipgroup.removeAllViews()
        }
        binding.medicineHistoryDelete.setOnClickListener {
            historyViewModel.removeMedicineHistory()
            binding.medicineList.removeAllViews()
        }
        //TODO
        // 저장된 검색 기록 만큼 칩 생성 - 완
        // 클릭하면 검색 창에 검색어 입력 + 검색 결과 화면으로 이동 + 검색 실행


        //TODO 클릭한 약 만큼 아이템을 뿌리든 뭘하든 리스트 만들어서 보여줘야함

        binding.apply {
            historyAdapter = HistoryRecyclerAdapter(object : HistoryRecyclerAdapter.ClickListener{
                override fun onItemClick(item: Medicine) {
                    val bottomSheetView = BottomsheetSearchDetailBinding.inflate(layoutInflater)
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    with(bottomSheetView) {
                        Glide.with(root).load(item.imageUrl ?: R.drawable.logo_big)
                            .into(detailIvMedicine)
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
                    bottomSheetDialog.setContentView(bottomSheetView.root)
                    bottomSheetDialog.show()
                }

                override fun onItemlLongClick(item: Medicine) {
                    TODO("Not yet implemented")
                }

                override fun isMedicineChecked(item: Medicine): Boolean {
//                    val result = mainViewModel.isMyMedicine(item.id!!)
//                    Log.d(TAG,"${item.id}: $result")
//                    return result
                    return false
                }

                override fun setMedicineChecked(item: Medicine, isChecked: Boolean) {
//                    val hasIt = mainViewModel.isMyMedicine(item.id!!)
//                    Log.d(TAG,"Do I have item:${item.id}? :$hasIt")
//                    if(hasIt) {
//                        Toast.makeText(requireContext(), "이미 복용 중인 약입니다.", Toast.LENGTH_SHORT).show()
//                    } else {
//                        MainNavigation.addFragment(
//                            TakeAddFragment.newInstance(item), FragmentTag.TakeAddFragment
//                        )
//                    }
                }
            })
            medicineList.adapter = historyAdapter
            historyAdapter.submitList(historyViewModel.medicineHistory.value)
            medicineList.itemAnimator = null

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.getQueryHistory()
        historyViewModel.getMedicineHistory()
        val queryHistory = historyViewModel.queryHistory
        val medicineHistory = historyViewModel.medicineHistory
        Log.d(TAG,medicineHistory.value!!.size.toString())

        Log.d(TAG,"히스토리 onResume ${medicineHistory.value.toString()}")

        if (queryHistory.value?.isNotEmpty() == true){
            binding.searchHistoryChipgroup.removeAllViews()
            for ( i in queryHistory.value!!){
                binding.searchHistoryChipgroup.addView(Chip(this.context).apply {
                    text = i
                    elevation = 8f
                    setChipDrawable(ChipDrawable.createFromAttributes(context,null,0,R.style.CustomChipStyle))
                    setOnClickListener {
                        historyViewModel.setSelectedChip(this.text.toString())
                    }
                    setOnCloseIconClickListener {
                        binding.searchHistoryChipgroup.removeView(this)
                        historyViewModel.removeQueryHistory()
                    }
                })
            }
        }

        historyAdapter.submitList(historyViewModel.medicineHistory.value)

    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "검색 기록"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            SearchHistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}