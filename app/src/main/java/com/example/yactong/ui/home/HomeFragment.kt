package com.example.yactong.ui.home

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.yactong.R
import com.example.yactong.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager : ViewPager2

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewpager = binding.searchVp
        homeViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val keyWords = listOf("머리","얼굴","목","가슴/흉부","복부","'등/허리","다리/발","피부")
        for (keyWord in keyWords) {
            val keyButton = Button(requireContext())
            keyButton.apply {
                text = keyWord
                textSize = 14F
                setTextColor(Color.WHITE)
                background = getDrawable(requireContext(),R.drawable.selector_key_button)
                isClickable = false
                setOnClickListener {
                    isClickable = !isClickable
                    setTextColor(Color.BLACK)
                }
            }
            binding.searchLoKeyword.addView(keyButton)
        }

        viewPager = binding.searchVp
        binding.searchVp.adapter = HomeViewPager(this@HomeFragment)

        TabLayoutMediator(binding.searchLoTab, binding.searchVp) { tab, position ->
            tab.text = HomeViewPager(this).pageTag[position]
        }.attach()

        binding.searchBtnSearch.setOnClickListener {
            val query = binding.searchEtSearchtext.text.toString()
            homeViewModel.searchPills(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}