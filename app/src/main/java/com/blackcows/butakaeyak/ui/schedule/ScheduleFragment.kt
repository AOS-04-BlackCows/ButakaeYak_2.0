package com.blackcows.butakaeyak.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentScheduleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.FriendViewPager
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
        if(curScheduleProfileId == userId) {
            return@ProfileRvAdapter
        }

        curScheduleProfileId = userId

        viewPagerAdapter.getFragment(binding.scheduleDetailViewPager, curScheduleProfileId)
    }

    private lateinit var viewPagerAdapter: FriendViewPager

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
                //TODO: open the friend dialog.
                //  and if scheduleProfile is changed, call the method: scheduleViewModel.getFriends(userId)
                if(userViewModel.user.value == null) {
                    Toast.makeText(requireContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "나중에 추가될 서비스입니다!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        initFriendsViewPager()
    }

    private fun initFriendsViewPager() {
        viewPagerAdapter = FriendViewPager(requireActivity())
        binding.scheduleDetailViewPager.adapter = viewPagerAdapter

        Log.d("ScheduleFragment", "view pager init done")

        if(userViewModel.user.value != null) {
            scheduleViewModel.getFriendProfile(userViewModel.user.value!!.id)
        }

        Log.d("ScheduleFragment", "view pager user check done")
    }

    private fun setObserver() {

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("UserViewModel", "ScheduleFragment: user is null? :${user==null}")

            if(user == null) {
                //binding.loginGuideCl.visibility = View.VISIBLE
//                val myScheduleProfile = ScheduleProfile("", "나", "")
//
//                resetViewPager(listOf(myScheduleProfile))

                scheduleViewModel.clearScheduleProfiles()
                viewPagerAdapter.clearAll()
            } else {
                curScheduleProfileId = user.id
                binding.loginGuideCl.visibility = View.GONE
                scheduleViewModel.getFriendProfile(user.id)
            }
        }

        scheduleViewModel.scheduleProfile.observe(viewLifecycleOwner) { friendsProfiles ->
            Log.d("ScheduleFragment", "check userNull?")
//            if(userViewModel.user.value == null && scheduleViewModel.scheduleProfile.value!!.isNotEmpty()) {
//                scheduleViewModel.clearScheduleProfiles()
//                Log.d("ScheduleFragment", "clear schedule profiles")
//                viewPagerAdapter.clearAll()
//                Log.d("ScheduleFragment", "viewPager clear")
//                return@observe
//            }

            val myScheduleProfile = if(userViewModel.user.value != null) with(userViewModel.user.value!!) {
                ScheduleProfile(id, name, profileUrl!!)
            } else ScheduleProfile("", "나", "")

            Log.d("ScheduleFragment", "make myScheduleProfile")

            val list = mutableListOf(myScheduleProfile).apply {
                addAll(friendsProfiles)
            }

            Log.d("ScheduleFragment", "make list")

            resetViewPager(list)

            Log.d("ScheduleFragment", "reset finish")
        }
    }

    private fun resetViewPager(list: List<ScheduleProfile>) {
        profileRvAdapter.submitList(list)

        Log.d("ScheduleFragment", "size: ${profileRvAdapter.currentList.size}")

        viewPagerAdapter.setScheduleProfiles(list)

        Log.d("ScheduleFragment", "set profile done")

        viewPagerAdapter.getFragment(binding.scheduleDetailViewPager, list[0])

        Log.d("ScheduleFragment", "getfragment done")
    }
}