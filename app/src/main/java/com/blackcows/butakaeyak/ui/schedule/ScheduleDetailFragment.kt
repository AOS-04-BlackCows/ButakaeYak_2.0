package com.blackcows.butakaeyak.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentScheduleDetailBinding
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter


class ScheduleDetailFragment : Fragment() {

    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!

    private val scheduleRvAdapter = ScheduleRvAdapter()

    private val userId by lazy {
        arguments?.getString(FRIEND_ID_DATA)
    }
    private val isMine by lazy {
        arguments?.getBoolean(IS_MINE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            todayAlarmRv.run {
                adapter = scheduleRvAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
            }

            openCalendarBtn.setOnClickListener {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FRIEND_ID_DATA = "FRIEND_ID_DATA"
        private const val IS_MINE = "IS_MINE"

        @JvmStatic
        fun newInstance(friendId: String, isMine: Boolean) =
            ScheduleDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FRIEND_ID_DATA, friendId)
                    putBoolean(IS_MINE, isMine)
                }
            }
    }

}