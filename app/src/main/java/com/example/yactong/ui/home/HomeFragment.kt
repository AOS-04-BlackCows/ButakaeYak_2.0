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
import com.example.yactong.data.models.SearchCategory
import com.example.yactong.data.models.SearchCategoryDataSource
import com.example.yactong.databinding.BottomsheetSearchfilterBinding
import com.example.yactong.databinding.FragmentHomeBinding
import com.example.yactong.databinding.ItemSearchfilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

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
        initView()

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

    private fun initView(){
        val bottomSheetView = BottomsheetSearchfilterBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(bottomSheetView.root)
        // 카테고리 클릭 시 변경
        val categoryArr = arrayOf(
            binding.searchCategory1, binding.searchCategory2, binding.searchCategory3, binding.searchCategory4,
            binding.searchCategory5, binding.searchCategory6, binding.searchCategory7, binding.searchCategory8,
        )
        for (i in categoryArr) {
            i.setOnCheckedChangeListener { chip, isChecked ->
                bottomSheetView.effectList.addView(clickedCategory(i,i.text.toString()))
                if (isChecked){
                    bottomSheetDialog.show()
                }else{
                    bottomSheetDialog.hide()

                }

            }
        }
    }
    private fun clickedCategory (view: View, title: String) : View {
        val filterItem = ItemSearchfilterBinding.inflate(layoutInflater)
        filterItem.effectTitle.text = title
        val selectStatus: SearchCategory = when (view) {
            binding.searchCategory1 -> SearchCategory.HEAD
            binding.searchCategory2 -> SearchCategory.FACE
            binding.searchCategory3 -> SearchCategory.NECK
            binding.searchCategory4 -> SearchCategory.CHEST_THORAX
            binding.searchCategory5 -> SearchCategory.ABDOMEN
            binding.searchCategory6 -> SearchCategory.BACK_WAIST
            binding.searchCategory7 -> SearchCategory.LEGS_FEET
            binding.searchCategory8 -> SearchCategory.SKIN
            else -> SearchCategory.HEAD
        }
        val keyWords = SearchCategoryDataSource.getSearchSubCategory(selectStatus)
        for ( k in keyWords){
            filterItem.effectChipgroup.addView(Chip(this.context).apply {
                text = k.name
                setChipDrawable(ChipDrawable.createFromAttributes(context, null, 0, R.style.CustomChipStyle_On))
                with(this) {
                    chipStrokeWidth = 0.0f
                    setOnClickListener {
                        binding.searchEtSearchtext.setText(k.name)
                        Log.d("chip누른다!!","이거 누름 ${k.name}")
                    }
                }
            })
        }
        return filterItem.root
    }
}