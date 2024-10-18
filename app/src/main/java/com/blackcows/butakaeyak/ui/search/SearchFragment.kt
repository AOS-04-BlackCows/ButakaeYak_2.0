package com.blackcows.butakaeyak.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.search.adapter.SearchViewPager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SearchFragment_Log"
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private val searchViewModel: SearchViewModel by activityViewModels()

    private var imm : InputMethodManager? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        imm =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val viewpager = binding.searchVp
        searchViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainNavigation.hideBottomNavigation(true)

        initUiState()

        viewPager = binding.searchVp
        binding.searchVp.adapter = SearchViewPager(this@SearchFragment)

        searchViewModel.selectedCip.observe(viewLifecycleOwner){
            binding.searchEtSearchtext.setText(it)
            searchQuery()
        }

        TabLayoutMediator(binding.searchLoTab, binding.searchVp) { tab, position ->
            tab.text = SearchViewPager(this).pageTag[position]
        }.attach()

        binding.searchBtnSearch.setOnClickListener {
            searchQuery()
        }
        binding.searchEtSearchtext.setOnEditorActionListener { v, actionId, event ->
            Log.d(TAG, "keyCode: ${actionId} -- ${EditorInfo.IME_ACTION_DONE} ")
            if(actionId == EditorInfo.IME_ACTION_DONE){
                searchQuery()
            }
            true
        }
    }
    private fun searchQuery(){
        val query = binding.searchEtSearchtext.text.toString()
        if(query.isNotEmpty()){
            viewPager.currentItem = 0
            binding.searchLoImageblock.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(binding.searchBtnSearch.windowToken, 0)
            searchViewModel.searchMedicinesWithName(query)
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken ?: null, 0)
        }
    }

    private fun initUiState() {
        lifecycleScope.launch {
            searchViewModel.uiState.collectLatest {
                when(it) {
                    is SearchUiState.SearchMedicinesSuccess -> {
                        binding.searchLoProgressContainer.visibility = View.GONE
                    }
                    is SearchUiState.Loading -> {
                        binding.searchLoProgressContainer.visibility = View.VISIBLE
                    }
                    else -> null
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        MainNavigation.hideBottomNavigation(false)

        _binding = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "검색 기록"

        @JvmStatic
        fun newInstance(columnCount: Int,) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

}