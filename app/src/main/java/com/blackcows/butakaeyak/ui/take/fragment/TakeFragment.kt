package com.blackcows.butakaeyak.ui.take.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentTakeBinding
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.adapter.TakeAdapter
import com.blackcows.butakaeyak.ui.take.data.CycleItem

class TakeFragment : Fragment(), TakeAdapter.OnItemDeleteListener {

    //binding 설정
    private var _binding: FragmentTakeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : TakeAdapter

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(false)

        _binding = FragmentTakeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_take_to_navigation_take_add)
            }
            adapter = TakeAdapter(this@TakeFragment,requireContext())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()
        }

        viewModel._cycleFragment.observe(viewLifecycleOwner, Observer { cycleItem ->
            cycleItem?.let {
                for(item in it)
                    adapter.updateData(it)
            }
        })
    }

    override fun onItemDelete(position: Int) {
        viewModel.removeCycleItem(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}