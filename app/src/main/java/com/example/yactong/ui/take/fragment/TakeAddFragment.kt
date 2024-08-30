package com.example.yactong.ui.take.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.yactong.databinding.FragmentTakeAddBinding
import com.example.yactong.ui.take.TakeViewModel
import com.example.yactong.ui.take.TakeViewPagerAdapter

class TakeAddFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentTakeAddBinding? = null
    private val binding get() = _binding!!

    //viewPager 설정
    private var viewPager : ViewPager2? = null

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.vp2Add
        binding.apply {
            if (viewPager?.adapter == null) {
                vp2Add.adapter = TakeViewPagerAdapter(this@TakeAddFragment)
                vp2Add.isUserInputEnabled = false
            }
            viewModel.currentPage.observe(viewLifecycleOwner){page ->
                viewPager?.currentItem = page
            }
        }
    }


}