package com.blackcows.butakaeyak.ui.home

import android.content.Intent
import androidx.fragment.app.viewModels
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
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPagerAdapter
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.blackcows.butakaeyak.ui.take.fragment.NameFragment
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.blackcows.butakaeyak.ui.textrecognition.OCR_Activity
import io.ktor.util.date.WeekDay
import java.time.LocalDate


private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    var openFlag = false
    private val homeViewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    //binding 설정
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//    private val item : MyMedicine? = null

    //ViewPager 설정
    private val homeViewPagerAdapter by lazy {
        HomeViewPagerAdapter {
            // mainViewModel.cancelFavoritePharmacy(it.name)
            // TODO mainViewModel에 약 예약으로 가기 추가하고 적용
            // TODO onClickItem 옵션추가
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

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
        val mockUpMedicineGroup = listOf(
            MedicineGroup("0001","그룹 1","1",listOf<MedicineDetail>(MedicineDetail("","","","","","","","")),listOf("커스텀 약","커스텀 약"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("07:50", "12:50"),listOf<String>()),
            MedicineGroup("0002","그룹 2","2",listOf<MedicineDetail>(MedicineDetail("","","","","","","","")),listOf(),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("01:10", "04:10"),listOf<String>()),
            MedicineGroup("0003","그룹 3","3",listOf<MedicineDetail>(MedicineDetail("","","","","","","","")),listOf("커스텀 약1","커스텀 약2"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("11:59", "14:59"),listOf<String>())
        )
//        감기약
//        종합비타민비타민D루테인밀크씨슬마그네슘아르기닌유산균오메가3
//        자기전_먹는_약



        homeViewPagerAdapter.submitList(mockUpMedicineGroup)
        binding.vpTodayMedicine.adapter = homeViewPagerAdapter
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(binding.vpTodayMedicine)

        //애니메이션 변수
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close)

        //초기 애니메이션은 닫혀있는 애니메이션
        binding.btnAddMedicineContainer.startAnimation(fab_close)

        binding.btnAddMedicineGroup.setOnClickListener(this)
        binding.btnAddMedicineContainer.setOnClickListener(this)

        binding.btnAddMedicineContainer.isClickable = false
        fragmentInit()
        //
//        fragList = arrayListOf(FragmentControl(), FragmentState(), FragmentPolicy())
//
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        return root
    }

    //클릭된 버튼에 의해 프래그먼트를 변경
    override fun onClick(v: View){
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when(v.id){
            R.id.btn_add_medicine_group -> anim()
            R.id.btn_add_medicine_container ->{
                anim()
//                for (i in 0 until fragList.size) { transaction.hide(fragList[i])}
//                transaction.show(fragList[0])
            }
        }
//        transaction.commit()
    }

    fun anim () {
        if (openFlag){
            // Fab이 눌려있을때 닫는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_close)
            binding.btnAddMedicineContainer.isClickable = false
            openFlag = false
        } else {
            // Fab이 눌리지 않았을 때 여는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_open)
            binding.btnAddMedicineContainer.isClickable = true
            openFlag = true
        }
    }
    /* TODO
        응애 플로팅버튼 만들어라 응애
        뷰모델 구성해보기
        책상정리하기
    */

    private fun fragmentInit () {
        // 나랑 가까운 약국
        binding.homeConnection1.setOnClickListener{
            Toast.makeText(context, "homeConnection1.onClicked", Toast.LENGTH_SHORT).show()
        }
        // 즐겨찾기한 약국
        binding.homeConnection2.setOnClickListener{
            Toast.makeText(context, "homeConnection2.onClicked", Toast.LENGTH_SHORT).show()
        }
        // 이게 무슨 약이지?
        binding.homeConnection3.setOnClickListener{
            Toast.makeText(context, "homeConnection3.onClicked", Toast.LENGTH_SHORT).show()
        }
        // 직접 등록
        binding.btnAddMedicine1.setOnClickListener{
            Toast.makeText(context, "btnAddMedicine1.onClicked", Toast.LENGTH_SHORT).show()
            MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragment)
            anim()
        }
        // 사진 등록
        binding.btnAddMedicine2.setOnClickListener{
            Toast.makeText(context, "btnAddMedicine2.onClicked.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireActivity(), OCR_Activity::class.java))
            anim()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}