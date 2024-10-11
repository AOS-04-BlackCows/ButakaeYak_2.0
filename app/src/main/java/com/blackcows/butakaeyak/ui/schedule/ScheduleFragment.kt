package com.blackcows.butakaeyak.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.databinding.FragmentScheduleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvAdapter
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import com.blackcows.butakaeyak.ui.viewmodels.FriendViewModel
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import java.time.LocalDate

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val friendViewModel: FriendViewModel by activityViewModels()

    private var selectedProfileId = ""

    private val profileRvAdapter = ProfileRvAdapter() { userId ->
        selectedProfileId = userId
        val isMine = if(userViewModel.user.value == null) true
                    else if(userViewModel.user.value!!.id == userId) true
                    else false

        val detailFragment = if(isMine)  MyScheduleDetailFragment()
                            else ScheduleDetailFragment.newInstance(userId)

        parentFragmentManager.beginTransaction()
            .replace(R.id.schedule_container_view, detailFragment)
            .commit()
    }

    private val profilesObserver = Observer<List<ScheduleProfile>> { profiles ->
        val myScheduleProfile = if(userViewModel.user.value != null) with(userViewModel.user.value!!) {
            ScheduleProfile(id, name, profileUrl!!)
        } else ScheduleProfile("", "나", "")

        Log.d("ScheduleFragment", "Cur State: ${lifecycle.currentState}")

        val list = mutableListOf(myScheduleProfile).apply {
            addAll(profiles)
        }

        profileRvAdapter.submitList(list)

        // when a selected profile is removed, show my profile.
        if(!profiles.any { it.userId == selectedProfileId }) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.schedule_container_view, MyScheduleDetailFragment())
                .commit()
        }
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

        setObserver()

        with(binding) {
            profileRv.run {
                adapter = profileRvAdapter
                addItemDecoration(ProfileRvDecoration.getLinearDecoSimpleItem())
            }

            profileAddBtn.setOnClickListener {
                if(userViewModel.user.value == null) {
                    Toast.makeText(requireContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "나중에 추가될 서비스입니다!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setObserver()
    }

    override fun onStop() {
        super.onStop()

        removeObserver()
    }

    private fun setObserver() {
        friendViewModel.friendProfiles.observe(viewLifecycleOwner, profilesObserver)
    }
    private fun removeObserver() {
        friendViewModel.friendProfiles.removeObserver(profilesObserver)
    }
}