package com.blackcows.butakaeyak.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentNoteGroupBinding

class NoteGroupFragment : Fragment() {

    private var _binding: FragmentNoteGroupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteGroupBinding.inflate(inflater)
        return binding.root
    }


}