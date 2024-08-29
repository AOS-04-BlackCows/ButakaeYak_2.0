package com.example.yactong.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.yactong.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewPager by lazy { HomeViewPager(this) }

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 계속 이용해야 할 수도 있으니 클래스 프로퍼티로 선언해주기.
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewpager = binding.homeVp
        homeViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeVp.adapter = homeViewPager
        TabLayoutMediator(binding.homeLoTab, binding.homeVp) { tab, position ->
            tab.text = homeViewPager.pageTag[position]
        }.attach()

        binding.homeBtnSearch.setOnClickListener {
            val query = binding.homeEtSearchtext.text.toString()
            homeViewModel.searchPills(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}