package com.blackcows.butakaeyak.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.HomeRvGroup
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeTodayMedicineRvAdapter
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPagerAdapter
import com.blackcows.butakaeyak.ui.map.MapFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.schedule.TimeToGroup
import com.blackcows.butakaeyak.ui.search.SearchFragment
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.blackcows.butakaeyak.ui.textrecognition.OcrFragment
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.delay
import java.time.LocalDate


private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    lateinit var fab_close_no_delay: Animation
    var openFlag = false

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    //binding 설정
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayMedicineGroupRvAdapter : HomeTodayMedicineRvAdapter
//    private val item : MyMedicine? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainViewModel.pharmacies.observe(viewLifecycleOwner) {
            Log.d("UserFragment", "pharmacies size: ${it.size}")
        }
        // MockUp Data
        /*
        뷰페이저 제거
        val mockUpMedicineGroup = listOf(
            MedicineGroup("0001","그룹 1","1",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약","커스텀 약"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("07:50", "12:50"),listOf<String>()),
            MedicineGroup("0002","그룹 2","2",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf(),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("01:10", "04:10"),listOf<String>()),
            MedicineGroup("0003","그룹 3","3",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약1","커스텀 약2"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("11:59", "14:59"),listOf<String>())
        )
        homeViewPagerAdapter.submitList(mockUpMedicineGroup)
        binding.vpTodayMedicine.adapter = homeViewPagerAdapter
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(binding.vpTodayMedicine)
        */

        //애니메이션 변수
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close)
        fab_close_no_delay = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close_no_delay)

        binding.btnAddMedicineGroup.setOnClickListener(this)
        binding.btnAddMedicineContainer.setOnClickListener(this)

        fragmentInit()
        //
//        fragList = arrayListOf(FragmentControl(), FragmentState(), FragmentPolicy())
//
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        /* 고려사항 정리
        1. 아이템의 출력은 alarm의 갯수만큼
        2. 아이템의 정렬 순서도 alarm으로

        */

        todayMedicineGroupRvAdapter = HomeTodayMedicineRvAdapter (object : HomeTodayMedicineRvAdapter.ClickListener {
            override fun onTodayMedicineClick(item: HomeRvGroup, position: Int) {
                // TODO 알람 그룹 클릭시 그룹 상세를 보여주는 화면으로 이동 - > 나중에 추가!
            }
            override fun onAlarmClick(item: HomeRvGroup, position: Int, isSelected: Boolean) {
                val userId= userViewModel.user.value?.id ?: "guest"
                homeViewModel.checkTakenMedicineGroup(userId, item.groupId, isSelected, item.alarmTime)
            }
        })
        binding.homeRvTodayMedicineGroup.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMedicineGroupRvAdapter
            itemAnimator = null
        }
        // 아직 데이터가 안들어옴
        homeViewModel.getTodayMedicine(userViewModel.user.value?.id?: "guest")

        binding.homeAlarmViewMore.setOnClickListener {
            val fullList = medicineGroupConverter(homeViewModel.medicineGroup.value!!)

            if(fullList.size < 2) return@setOnClickListener

            if (todayMedicineGroupRvAdapter.currentList.size == 2) {
                todayMedicineGroupRvAdapter.submitList(fullList)
                binding.homeAlarmViewMore.setText(R.string.view_close)
            } else {
                todayMedicineGroupRvAdapter.submitList(fullList.take(2))
                binding.homeAlarmViewMore.setText(R.string.view_more)
            }
        }
        Log.d(TAG,"mainViewModel.getMyMedicineList() = ${mainViewModel.myMedicines.value}")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.medicineGroup.observe(viewLifecycleOwner) {
            Log.d(TAG, "homeViewModel.medicineGroup.value = $it")

            val homeRvGroups = mutableListOf<HomeRvGroup>()
            val alarmMap = mutableMapOf<String, MutableList<MedicineGroup>>()
            it.forEach { group ->
                group.alarms.forEach { alarm ->
                    alarmMap.getOrPut(alarm) { mutableListOf() }.add(group)
                }
            }
            it.forEach { group ->
                group.alarms.forEach { alarm ->
                    val todayTakenFormat = "${LocalDate.now()} $alarm"
                    val isTaken = group.hasTaken.contains(todayTakenFormat)
                    homeRvGroups.add(
                        HomeRvGroup(
                            groupId = group.id,
                            alarmTime = alarm,
                            groupName = group.name,
                            isHasTakenTime = isTaken
                        )
                    )
                }
            }

            todayMedicineGroupRvAdapter.submitList(homeRvGroups.sortedBy { it.alarmTime }.take(2))
        }
    }

    // 상단 리싸이클러 뷰에 들어갈 데이터를 새로 만든 후 갱신
    private fun medicineGroupConverter (list: List<MedicineGroup>): List<HomeRvGroup> {
        val medicineGroupList = mutableListOf<HomeRvGroup>()

        for (i in list) {
            for (j in i.alarms) {
                val hasTakenFormat = "${LocalDate.now()} $j"
                medicineGroupList += HomeRvGroup(i.id, j, i.name, i.hasTaken.contains(hasTakenFormat))
            }
        }

        medicineGroupList.sortBy { it.alarmTime }
        return medicineGroupList
    }


    //클릭된 버튼에 의해 프래그먼트를 변경
    override fun onClick(v: View){
        when(v.id){
            R.id.btn_add_medicine_group -> anim()
            R.id.btn_add_medicine_container -> anim()
        }
    }

    fun anim () {
        if (openFlag){
            // Fab이 눌려있을때 닫는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_close)
            binding.btnAddMedicineContainer.isClickable = false
            binding.btnAddMedicine1.isClickable = false
            binding.btnAddMedicine2.isClickable = false
            openFlag = false
            binding.btnAddMedicineContainer.visibility = View.GONE
        } else {
            // Fab이 눌리지 않았을 때 여는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_open)
            binding.btnAddMedicineContainer.isClickable = true
            binding.btnAddMedicine1.isClickable = true
            binding.btnAddMedicine2.isClickable = true
            openFlag = true
            binding.btnAddMedicineContainer.visibility = View.VISIBLE
        }
    }

    private fun fragmentInit () {
        // 나랑 가까운 약국
        binding.homeConnection1.setOnClickListener{
            MainNavigation.addFragment(MapFragment(), FragmentTag.MapFragment)
        }
        // 이게 무슨 약이지?
        binding.homeConnection2.setOnClickListener{
            MainNavigation.addFragment(SearchFragment(), FragmentTag.SearchFragment)
        }
        // 직접 등록
        binding.btnAddMedicine1.setOnClickListener{
            MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragment)
        }
        // 사진 등록
        binding.btnAddMedicine2.setOnClickListener{
            MainNavigation.addFragment(OcrFragment(), FragmentTag.OCRFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        // Fab이 눌려있을때 닫는 애니메이션
        binding.btnAddMedicineContainer.startAnimation(fab_close_no_delay)
        binding.btnAddMedicineContainer.isClickable = false
        binding.btnAddMedicine1.isClickable = false
        binding.btnAddMedicine2.isClickable = false
        openFlag = false
        binding.btnAddMedicineContainer.visibility = View.GONE
    }
    override fun onResume() {
        super.onResume()
        homeViewModel.getTodayMedicine(userViewModel.user.value?.id ?: "guest")
    }
}