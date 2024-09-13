package com.blackcows.butakaeyak.ui.home

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
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPagerAdapter
import com.google.android.material.tabs.TabLayout
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

    //ViewPager 설정
    private val homeViewPagerAdapter by lazy {
        HomeViewPagerAdapter {
            // mainViewModel.cancelFavoritePharmacy(it.name)
            // TODO mainViewModel에 약 예약으로 가기 추가하고 적용
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
            MedicineGroup("감기약","","","","","","","","AM 07:50"),
            MedicineGroup("종합비타민비타민D루테인밀크씨슬마그네슘아르기닌유산균오메가3","","","","","","","","PM 01:10"),
            MedicineGroup("자기전 먹는 약","","","","","","","","PM 11:59")
        )
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

    fun anim(){
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}