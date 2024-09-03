package com.blackcows.butakaeyak.ui.home

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
import com.blackcows.butakaeyak.databinding.FragmentPillResultBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeRecyclerAdapter
import com.blackcows.butakaeyak.ui.home.placeholder.PlaceholderContent

class PillResultFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentPillResultBinding?=null
    private val binding get() = _binding!!

    private lateinit var adapter : HomeRecyclerAdapter

    //viewModel 설정
    private val homeViewModel : HomeViewModel by activityViewModels()

    private var columnCount = 1 //컬럼 갯수 = 1 리니어

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPillResultBinding.inflate(inflater,container,false)
        val root = binding.root

        // Set the adapter
        if (root is RecyclerView) {
            with(root) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            adapter = HomeRecyclerAdapter(PlaceholderContent.ITEMS)
            resultlist.adapter = adapter
            resultlist.itemAnimator = null
            //TODO 여기 수정해야될듯...왜 리스트가 안뜨지???

        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "결과 화면"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            PillResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}