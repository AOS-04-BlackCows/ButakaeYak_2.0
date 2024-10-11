package com.blackcows.butakaeyak.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.databinding.FragmentNoteGroupBinding
import com.blackcows.butakaeyak.ui.note.recycler.NoteItemRvAdapter
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.viewmodels.MemoViewModel

class NoteGroupFragment : Fragment() {

    private var _binding: FragmentNoteGroupBinding? = null
    private val binding get() = _binding!!

    private val memoViewModel: MemoViewModel by activityViewModels()
    private val noteRvAdapter = NoteItemRvAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteGroupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerView) {
            adapter = noteRvAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
        }

        memoViewModel.memos.observe(viewLifecycleOwner) {
            noteRvAdapter.submitList(memoViewModel.getGroupMemos())
        }
    }
}