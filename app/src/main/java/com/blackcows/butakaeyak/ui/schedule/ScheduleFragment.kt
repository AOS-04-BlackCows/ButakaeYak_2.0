package com.blackcows.butakaeyak.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentScheduleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvAdapter
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.blackcows.butakaeyak.ui.take.fragment.CycleFragment

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    private val profileRvAdapter = ProfileRvAdapter() { id ->
        scheduleViewModel.getMedicineGroup(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleViewModel.getFriends()

        with(binding) {
            profileRv.run {
                adapter = profileRvAdapter
                addItemDecoration(ProfileRvDecoration.getLinearDecoSimpleItem())
            }

            profileAddBtn.setOnClickListener {
                //TODO: open the friend dialog.
            }
        }
    }



}