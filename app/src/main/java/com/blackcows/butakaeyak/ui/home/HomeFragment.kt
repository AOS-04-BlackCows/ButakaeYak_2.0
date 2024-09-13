package com.blackcows.butakaeyak.ui.home

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
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
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.SearchCategory
import com.blackcows.butakaeyak.data.models.SearchCategoryDataSource
import com.blackcows.butakaeyak.databinding.BottomsheetSearchfilterBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.databinding.ItemSearchfilterBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment_Log"
class HomeFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        Log.d("HomeFragment", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}