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
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.blackcows.butakaeyak.ui.take.fragment.CycleFragment
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private var curScheduleProfileId = ""

    private val profileRvAdapter = ProfileRvAdapter() { userId ->
        curScheduleProfileId = userId
        val isMine = if(userViewModel.user.value == null) true
                    else if(userViewModel.user.value!!.id == userId) true
                    else false

        Log.d("ScheduleFragment", "click!")

        val detailFragment = ScheduleDetailFragment.newInstance(userId, isMine)
        parentFragmentManager.beginTransaction()
            .replace(R.id.schedule_container_view, detailFragment)
            .commit()
    }

    private lateinit var userObserver: Observer<User?>
    private lateinit var scheduleObserver: Observer<List<ScheduleProfile>>


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

    override fun onResume() {
        super.onResume()

        setObserver()
        initProfiles()
    }

    override fun onPause() {
        super.onPause()
        detachObserver()
    }

    private fun initProfiles() {
        val myScheduleProfile: ScheduleProfile
        if(userViewModel.user.value != null) with(userViewModel.user.value!!) {
            scheduleViewModel.getFriendProfile(id)
            return
        } else {
            myScheduleProfile = ScheduleProfile("", "나", "")
        }

        Log.d("ScheduleFragment", "replace on initProfiles.")

        profileRvAdapter.submitList(listOf(myScheduleProfile))

        val detailFragment = ScheduleDetailFragment.newInstance(myScheduleProfile.userId, true)
        parentFragmentManager.beginTransaction()
            .replace(R.id.schedule_container_view, detailFragment)
            .commit()
    }

    private fun setObserver() {
        userObserver = Observer { user ->
            Log.d("UserViewModel", "ScheduleFragment: user is null? :${user==null}")
            curScheduleProfileId = user?.id ?: ""
        }
        userViewModel.user.observe(viewLifecycleOwner, userObserver)

        scheduleObserver = Observer { friendsProfiles ->
            val myScheduleProfile = if(userViewModel.user.value != null) with(userViewModel.user.value!!) {
                ScheduleProfile(id, name, profileUrl!!)
            } else ScheduleProfile("", "나", "")

            Log.d("ScheduleFragment", "make myScheduleProfile")

            val list = mutableListOf(myScheduleProfile).apply {
                addAll(friendsProfiles)
            }

            profileRvAdapter.submitList(list)

            val detailFragment = ScheduleDetailFragment.newInstance(myScheduleProfile.userId, true)
            parentFragmentManager.beginTransaction()
                .replace(R.id.schedule_container_view, detailFragment)
                .commit()
        }
        scheduleViewModel.scheduleProfile.observe(viewLifecycleOwner, scheduleObserver)

    }

    private fun detachObserver() {
        scheduleViewModel.scheduleProfile.removeObserver(scheduleObserver)
        //userViewModel.user.removeObserver(userObserver)
    }
}