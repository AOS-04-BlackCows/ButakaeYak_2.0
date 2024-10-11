package com.blackcows.butakaeyak.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.databinding.FragmentNoteBinding
import com.blackcows.butakaeyak.ui.viewmodels.MemoViewModel
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val memoViewModel: MemoViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        initView()
        if(userViewModel.user.value == null) {
            binding.loginGuideCl.visibility = View.VISIBLE
        } else {
            binding.loginGuideCl.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        if(userViewModel.user.value != null) {
            Toast.makeText(requireContext(), "나중에 추가될 기능입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        val tabLayout = binding.tabLo
        val noteAdapter = NoteViewPager(requireActivity())
        val viewPager = binding.viewpager.apply {
            adapter = noteAdapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = noteAdapter.tabName[position]
        }.attach()
    }

    private fun setObserver() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("UserViewModel", "NoteFragment: user is null? :${user==null}")
            if(user == null) {
                binding.loginGuideCl.visibility = View.VISIBLE
            } else {
                binding.loginGuideCl.visibility = View.GONE
            }
        }
    }
}