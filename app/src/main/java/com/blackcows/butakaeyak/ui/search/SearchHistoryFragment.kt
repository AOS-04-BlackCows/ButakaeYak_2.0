package com.blackcows.butakaeyak.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.BottomsheetSearchDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchHistoryBinding
import com.blackcows.butakaeyak.ui.search.adapter.HistoryRecyclerAdapter
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
        val medicineHistory = historyViewModel.medicineHistory

        binding.searchHistoryDelete.setOnClickListener {
            historyViewModel.removeQueryHistory()
            binding.searchHistoryChipgroup.removeAllViews()
        }
        binding.medicineHistoryDelete.setOnClickListener {
            historyViewModel.removeMedicineHistory()
            historyAdapter.submitList(listOf())
        }

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
            })
            medicineList.adapter = historyAdapter
            Log.d(TAG, "onCreateView medicineHistory ${ medicineHistory.value.toString() }")
            medicineHistory.observe(viewLifecycleOwner){
                if(it.isNotEmpty()) Log.d(TAG, "Class: ${it[0]::class.simpleName}")
                historyAdapter.submitList(it)
            }
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

        queryHistory.value?.let { queries ->
            if (queries.isNotEmpty()) {
                binding.searchHistoryChipgroup.removeAllViews()
                queries.forEach { query ->
                    binding.searchHistoryChipgroup.addView(Chip(this.context).apply {
                        text = query
                        elevation = 8f
                        setChipDrawable(ChipDrawable.createFromAttributes(context, null, 0, R.style.CustomChipStyle))
                        setOnClickListener {
                            historyViewModel.setSelectedChip(this.text.toString())
                        }
                    })
                }
            }
        }
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