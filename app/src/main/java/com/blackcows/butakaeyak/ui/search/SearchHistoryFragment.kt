package com.blackcows.butakaeyak.ui.search

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchHistoryBinding
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter
import com.google.android.material.chip.Chip

private const val TAG = "검색 기록"
class SearchHistoryFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentSearchHistoryBinding? = null
    private val binding get() = _binding!!

    private var searchBinding: FragmentSearchBinding? = null

    private val historyViewModel: SearchViewModel by activityViewModels()

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

        val queryHistory = historyViewModel.queryHistory
        val medicineHistory = historyViewModel.medicineHistory
        fragmentInit()
        binding.searchHistoryDelete.setOnClickListener {
            binding.searchHistoryChipgroup.removeAllViews()
        }
        binding.medicineHistoryDelete.setOnClickListener {
            //TODO 내역 삭제하는거 만들어야 함....
        }
        //TODO
        // 저장된 검색 기록 만큼 칩 생성
        // 클릭하면 검색 창에 검색어 입력 + 검색 결과 화면으로 이동 + 검색 실행
        lateinit var searchChip : Chip

        if (queryHistory.value?.isNotEmpty() == true){
            for ( i in queryHistory.value!!){
                searchChip.text = i
                searchChip.setOnClickListener {
                    searchBinding!!.searchEtSearchtext.setText(searchChip.text)
                }
                binding.searchHistoryChipgroup.addView(searchChip)
            }
        }
        //TODO 클릭한 약 만큼아이탬을 뿌리든 뭘하든 리스트 만들어서 보여줘야함

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }

    private fun fragmentInit () {
        historyViewModel.getQueryHistory()
        historyViewModel.getMedicineHistory()
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